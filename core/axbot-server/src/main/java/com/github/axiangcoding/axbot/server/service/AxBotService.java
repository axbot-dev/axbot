package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.bot.kook.KookClient;
import com.github.axiangcoding.axbot.bot.kook.service.entity.GuildViewResp;
import com.github.axiangcoding.axbot.server.service.axbot.AxbotCommand;
import com.github.axiangcoding.axbot.server.service.axbot.SystemInputCallback;
import com.github.axiangcoding.axbot.server.service.axbot.UserInputCallback;
import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotInput;
import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotOutput;
import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotSysInput;
import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotSysOutput;
import com.github.axiangcoding.axbot.server.service.axbot.entity.kook.AxBotInputForKook;
import com.github.axiangcoding.axbot.server.service.axbot.entity.kook.AxBotOutputForKook;
import com.github.axiangcoding.axbot.server.service.axbot.entity.kook.AxBotSysInputForKook;
import com.github.axiangcoding.axbot.server.service.axbot.entity.kook.AxBotSysOutputForKook;
import com.github.axiangcoding.axbot.server.service.axbot.handler.AxBotSystemEvent;
import com.github.axiangcoding.axbot.server.service.axbot.handler.kook.AxBotHandlerForKook;
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
    public AxBotOutput genResponseForInput(int replyPlatform, AxBotInput input) {
        if (replyPlatform == PLATFORM_KOOK) {
            AxBotInputForKook in = ((AxBotInputForKook) input);
            AxBotOutputForKook out = new AxBotOutputForKook();
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
                    out.setContent(axBotHandlerForKook.notMatch(command));
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
                } else if (jc == AxbotCommand.COMMAND_GROUP_STATUS) {
                    out.setContent(axBotHandlerForKook.getGuildStatus(in.getFromGuild()));
                } else {
                    out.setContent(axBotHandlerForKook.notMatch(command));
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
    public void genResponseForInputAsync(int replyPlatform, AxBotInput input, UserInputCallback callback) {
        threadPoolTaskExecutor.execute(() -> {
            AxBotOutput output = genResponseForInput(replyPlatform, input);
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
                    GuildViewResp guildInfo = kookClient.getGuildView(fromGuild);
                    String defaultChannelId = guildInfo.getData().getDefaultChannelId();
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
