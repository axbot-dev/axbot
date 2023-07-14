package com.github.axiangcoding.axbot.app.bot.function;

import com.github.axiangcoding.axbot.app.bot.annotation.AxActiveFunc;
import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.ActiveEvent;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.enums.UserCmd;
import com.github.axiangcoding.axbot.app.server.configuration.exception.BusinessException;
import com.github.axiangcoding.axbot.app.server.controller.entity.CommonError;
import com.github.axiangcoding.axbot.app.server.data.entity.EndGuild;
import com.github.axiangcoding.axbot.app.server.data.entity.EndUser;
import com.github.axiangcoding.axbot.app.server.service.EndGuildService;
import com.github.axiangcoding.axbot.app.server.service.EndUserService;
import com.github.axiangcoding.axbot.app.server.service.TextCensorService;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.application.Application;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.component.kook.KookBotManager;
import love.forte.simbot.component.qguild.QQGuildBotManager;
import love.forte.simbot.event.ChannelMessageEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class FunctionHandler {
    @Resource
    ApplicationContext applicationContext;

    @Resource
    Application application;

    @Resource
    EndUserService endUserService;

    @Resource
    EndGuildService endGuildService;

    @Resource
    TextCensorService textCensorService;

    @WithSpan
    public void triggerEvent(ActiveEvent event, BotPlatform platform, Map<String, Object> params) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(AxActiveFunc.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            AbstractActiveFunction func = applicationContext.getBean(entry.getKey(), AbstractActiveFunction.class);
            AxActiveFunc annotation = func.getClass().getAnnotation(AxActiveFunc.class);
            if (annotation.event().equals(event)) {
                for (BotManager<?> botManager : application.getBotManagers()) {
                    if (platform == BotPlatform.KOOK && botManager instanceof KookBotManager kookBotManager) {
                        func.processByKOOK(kookBotManager.all().get(0).getBot(), params);
                    }
                    if (platform == BotPlatform.QQ_GUILD && botManager instanceof QQGuildBotManager qqGuildBotManager) {
                        func.processByQG(qqGuildBotManager.all().get(0).getBot(), params);
                    }
                }
            }
        }
    }

    @WithSpan
    public void processPassiveFunction(ChannelMessageEvent event, BotPlatform platform) {
        try {
            String eventId = event.getId().toString();
            String guildId = event.getChannel().getGuildId().toString();
            String channelId = event.getChannel().getId().toString();
            String authorId = event.getAuthor().getId().toString();
            String plainText = event.getMessageContent().getPlainText();
            UserCmd command = UserCmd.judgeCommand(plainText);
            // 创建或者获取终端用户表和频道表
            EndGuild endGuild = endGuildService.getOrCreate(guildId, platform);
            EndUser endUser = endUserService.getOrCreate(authorId, platform);
            // 记录用户输入记录
            long recordId = endUserService.recordInput(platform, guildId, channelId, authorId, plainText, command);
            endUserService.updateUsage(endUser);
            endGuildService.updateUsage(endGuild);

            AbstractPassiveFunction func;
            // 检查频道封禁状态
            if (endGuildService.isBlocked(endGuild)) {
                func = findInteractiveFunc(UserCmd.GUILD_BANNED);
                executePassiveFunction(platform, func, event);
                return;
            }
            // 检查用户封禁状态
            if (endUserService.isBlocked(endUser)) {
                func = findInteractiveFunc(UserCmd.USER_BANNED);
                executePassiveFunction(platform, func, event);
                return;
            }

            // 检查频道使用是否超出限制
            int guildInputLimit = endGuildService.getInputLimit(guildId, platform);
            if (endGuild.getUsage().getInputToday() > guildInputLimit) {
                func = findInteractiveFunc(UserCmd.GUILD_USAGE_LIMIT);
                executePassiveFunction(platform, func, event);
                return;
            }

            // 检查用户使用是否超出限制
            int userInputLimit = endUserService.getInputLimit(authorId, platform);
            if (endUser.getUsage().getInputToday() > userInputLimit) {
                func = findInteractiveFunc(UserCmd.USER_USAGE_LIMIT);
                executePassiveFunction(platform, func, event);
                return;
            }

            // 检查文本内容
            boolean textPassCheck = textCensorService.checkAndCacheText(plainText);
            if (!textPassCheck) {
                endUserService.markInputSensitive(recordId);
                func = findInteractiveFunc(UserCmd.CENSOR_FAILED);
                executePassiveFunction(platform, func, event);
                return;
            }

            // 正常的命令处理逻辑
            func = findInteractiveFunc(command);
            executePassiveFunction(platform, func, event);
        } catch (Exception e) {
            log.error("process passive function error", e);
            if (platform == BotPlatform.KOOK) {
                findInteractiveFunc(UserCmd.ERROR).processForKOOK(event);
            } else if (platform == BotPlatform.QQ_GUILD) {
                findInteractiveFunc(UserCmd.ERROR).processForQG(event);
            }
        }
    }

    private AbstractPassiveFunction findInteractiveFunc(UserCmd command) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(AxPassiveFunc.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            AbstractPassiveFunction func = applicationContext.getBean(entry.getKey(), AbstractPassiveFunction.class);
            AxPassiveFunc annotation = func.getClass().getAnnotation(AxPassiveFunc.class);
            if (annotation.command().equals(command)) {
                return func;
            }
        }
        throw new BusinessException(CommonError.ERROR, "no such interactive function: " + command.toCommand());
    }

    private void executePassiveFunction(BotPlatform platform, AbstractPassiveFunction func, ChannelMessageEvent event) {
        if (platform == BotPlatform.KOOK) {
            func.processForKOOK(event);
        } else if (platform == BotPlatform.QQ_GUILD) {
            func.processForQG(event);
        }
    }
}
