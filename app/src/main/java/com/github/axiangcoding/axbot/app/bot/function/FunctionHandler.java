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
                        func.processByKOOK(qqGuildBotManager.all().get(0).getBot(), params);
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
            // FIXME 检查封禁状态


            // FIXME 检查频道封禁状态

            // FIXME 检查是否超出限制

            // FIXME 检查输入的敏感词

            // 记录用户输入记录
            long recordId = endUserService.recordInput(platform, guildId, channelId, authorId, plainText, command);

            // 正常的命令处理逻辑
            AbstractPassiveFunction func = findInteractiveFunc(command);
            if (platform == BotPlatform.KOOK) {
                processPassiveFunctionForKook(func, event);
            } else if (platform == BotPlatform.QQ_GUILD) {
                processPassiveFunctionForQGuild(func, event);
            }
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

    private void processPassiveFunctionForKook(AbstractPassiveFunction func, ChannelMessageEvent event) {
        func.processForKOOK(event);

    }

    private void processPassiveFunctionForQGuild(AbstractPassiveFunction func, ChannelMessageEvent event) {
        func.processForQG(event);
    }


}
