package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.bot.kook.KookClient;
import com.github.axiangcoding.axbot.bot.kook.service.entity.KookResponse;
import com.github.axiangcoding.axbot.bot.kook.service.entity.KookGuild;
import com.github.axiangcoding.axbot.engine.AxbotCommand;
import com.github.axiangcoding.axbot.engine.SystemInputCallback;
import com.github.axiangcoding.axbot.engine.UserInputCallback;
import com.github.axiangcoding.axbot.engine.entity.*;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotSysInputForKook;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotSysOutputForKook;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotUserInputForKook;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotUserOutputForKook;
import com.github.axiangcoding.axbot.server.service.axbot.AxBotHandlerForKook;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class AxBotService {
    public static final Integer PLATFORM_KOOK = 1;
    public static final Integer PLATFORM_QQ = 2;


    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    AxBotHandlerForKook axBotHandlerForKook;

    @Resource
    KookGuildSettingService kookGuildSettingService;

    @Resource
    KookClient kookClient;

    /**
     * 同步响应用户输入
     *
     * @param input
     * @return
     */
    public AxBotUserOutput genResponseForInput(int replyPlatform, AxBotUserInput input) {
        if (replyPlatform == PLATFORM_KOOK) {
            AxBotUserInputForKook in = ((AxBotUserInputForKook) input);
            AxBotUserOutputForKook out = new AxBotUserOutputForKook();
            String userId = in.getFromUserId();
            out.setReplayToUser(userId);
            out.setReplayToMsg(in.getFromMsgId());
            out.setToChannel(in.getFromChannel());

            String command = in.getRequestCommand();
            String[] cList = StringUtils.split(command);

            if (StringUtils.isBlank(command)) {
                out.setContent(axBotHandlerForKook.getDefault());
            } else {
                AxbotCommand jc = AxbotCommand.judgeCommand(command);
                if (jc == null) {
                    out.setContent(axBotHandlerForKook.commandNotFound(command));
                } else if (jc == AxbotCommand.COMMAND_HELP) {
                    out.setContent(axBotHandlerForKook.getHelp());
                } else if (jc == AxbotCommand.COMMAND_VERSION) {
                    out.setContent(axBotHandlerForKook.getVersion());
                } else if (jc == AxbotCommand.COMMAND_LUCKY) {
                    String s = userId + LocalDate.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_DATE);
                    out.setContent(axBotHandlerForKook.getTodayLucky(s.hashCode()));
                } else if (jc == AxbotCommand.COMMAND_WT_QUERY_PROFILE) {
                    out.setContent(axBotHandlerForKook.queryWTProfile(cList[2], out));
                } else if (jc == AxbotCommand.COMMAND_WT_UPDATE_PROFILE) {
                    out.setContent(axBotHandlerForKook.updateWTProfile(cList[2], out));
                } else if (jc == AxbotCommand.COMMAND_GUILD_STATUS) {
                    out.setContent(axBotHandlerForKook.getGuildStatus(in.getFromGuild()));
                } else if (jc == AxbotCommand.COMMAND_GUILD_MANAGE) {
                    out.setContent(axBotHandlerForKook.manageGuild(
                            in.getFromUserId(), in.getFromGuild(), in.getFromChannel(), command));
                } else {
                    out.setContent(axBotHandlerForKook.commandNotFound(command));
                }
            }

            return out;
        } else if (replyPlatform == PLATFORM_QQ) {
            // TODO
        }
        return null;
    }

    /**
     * 异步响应用户输入
     *
     * @param input
     * @param callback
     */
    public void genResponseForInputAsync(int replyPlatform, AxBotUserInput input, UserInputCallback callback) {
        threadPoolTaskExecutor.execute(() -> {
            AxBotUserOutput output = genResponseForInput(replyPlatform, input);
            callback.callback(output);
        });
    }

    /**
     * 同步响应系统事件
     *
     * @param replyPlatform
     * @param input
     * @return
     */
    public AxBotSysOutput genResponseForSystem(int replyPlatform, AxBotSysInput input) {
        if (replyPlatform == PLATFORM_KOOK) {
            AxBotSysInputForKook in = (AxBotSysInputForKook) input;
            AxBotSysOutputForKook out = new AxBotSysOutputForKook();


            AxBotSystemEvent event = input.getEvent();
            final String fromGuild = in.getFromGuild();
            switch (event) {
                case SYSTEM_EVENT_EXIT_GUILD -> {
                    log.info("bot exit guild [{}]", fromGuild);
                    axBotHandlerForKook.exitGuild(fromGuild);
                    return null;
                }
                case SYSTEM_EVENT_JOIN_GUILD -> {
                    log.info("bot join guild [{}]", fromGuild);
                    String content = axBotHandlerForKook.joinGuild(fromGuild);
                    KookResponse<KookGuild> guildView = kookClient.getGuildView(fromGuild);
                    String defaultChannelId = guildView.getData().getDefaultChannelId();
                    out.setToChannel(defaultChannelId);
                    out.setContent(content);
                }
                default -> log.warn("not support yet");
            }
            return out;
        } else if (replyPlatform == PLATFORM_QQ) {
            // TODO
        } else {

        }
        return null;
    }

    /**
     * 异步响应系统事件
     *
     * @param replyPlatform
     * @param input
     * @param callback
     */
    public void genResponseForSystemAsync(int replyPlatform, AxBotSysInput input, SystemInputCallback callback) {
        threadPoolTaskExecutor.execute(() -> {
            AxBotSysOutput output = genResponseForSystem(replyPlatform, input);
            callback.callback(output);
        });
    }


}
