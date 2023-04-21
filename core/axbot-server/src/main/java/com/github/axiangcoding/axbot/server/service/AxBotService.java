package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.engine.AxbotCommand;
import com.github.axiangcoding.axbot.engine.SystemInputCallback;
import com.github.axiangcoding.axbot.engine.UserInputCallback;
import com.github.axiangcoding.axbot.engine.entity.*;
import com.github.axiangcoding.axbot.engine.entity.cqhttp.AxBotUserInputForCqhttp;
import com.github.axiangcoding.axbot.engine.entity.cqhttp.AxBotUserOutputForCqhttp;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotSysInputForKook;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotSysOutputForKook;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotUserInputForKook;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotUserOutputForKook;
import com.github.axiangcoding.axbot.remote.kook.KookClient;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookGuild;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookResponse;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.service.axbot.AxBotHandlerForCqhttp;
import com.github.axiangcoding.axbot.server.service.axbot.AxBotHandlerForKook;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AxBotService {

    @Resource
    BotConfProps botConfProps;

    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    AxBotHandlerForKook axBotHandlerForKook;

    @Resource
    AxBotHandlerForCqhttp axBotHandlerForCqhttp;

    @Resource
    KookGuildSettingService kookGuildSettingService;

    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    TextCensorService textCensorService;

    @Resource
    KookClient kookClient;

    @Resource
    UserInputRecordService userInputRecordService;

    public boolean isTriggerMessage(String message) {
        if (StringUtils.isBlank(message)) {
            return false;
        }
        String[] contentSplit = StringUtils.split(message);
        List<String> prefixes = botConfProps.getTriggerMessagePrefix();
        boolean isTrigger = false;
        for (String prefix : prefixes) {
            if (prefix.equals(contentSplit[0])) {
                isTrigger = true;
                break;
            }
        }
        return isTrigger;
    }

    /**
     * 同步响应用户输入
     *
     * @param input
     * @return
     */
    public AxBotUserOutput genResponseForInput(AxBotSupportPlatform replyPlatform, AxBotUserInput input) {
        if (replyPlatform == AxBotSupportPlatform.PLATFORM_KOOK) {
            return processKookUserEvent(input);
        } else if (replyPlatform == AxBotSupportPlatform.PLATFORM_CQHTTP) {
            return processCqhttpUserEvent(input);
        }
        return null;
    }

    /**
     * 异步响应用户输入
     *
     * @param input
     * @param callback
     */
    public void genResponseForInputAsync(AxBotSupportPlatform replyPlatform, AxBotUserInput input, UserInputCallback callback) {
        threadPoolTaskExecutor.execute(() -> {
            try {
                AxBotUserOutput output = genResponseForInput(replyPlatform, input);
                callback.callback(output);
            } catch (Exception e) {
                log.error("generate response for user input async error", e);
                callback.catchException(e);
            }
        });
    }

    /**
     * 同步响应系统事件
     *
     * @param replyPlatform
     * @param input
     * @return
     */
    public AxBotSysOutput genResponseForSystem(AxBotSupportPlatform replyPlatform, AxBotSysInput input) {
        if (replyPlatform == AxBotSupportPlatform.PLATFORM_KOOK) {
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
                case SYSTEM_EVENT_BILI_ROOM_REMIND -> {
                    log.info("send bilibili live remainder");
                    Map<String, Object> extraMap = in.getExtraMap();
                    String content = axBotHandlerForKook.biliLiveRemind(
                            (Long) extraMap.get("roomId"),
                            (String) extraMap.get("title"),
                            (String) extraMap.get("areaName"),
                            (String) extraMap.get("description"));
                    out.setContent(content);
                }
                default -> log.warn("not support yet");
            }
            return out;
        } else if (replyPlatform == AxBotSupportPlatform.PLATFORM_CQHTTP) {
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
    public void genResponseForSystemAsync(AxBotSupportPlatform replyPlatform, AxBotSysInput input, SystemInputCallback callback) {
        threadPoolTaskExecutor.execute(() -> {
            try {
                AxBotSysOutput output = genResponseForSystem(replyPlatform, input);
                callback.callback(output);
            } catch (Exception e) {
                log.error("generate response for system input async error", e);
            }
        });
    }

    private AxBotUserOutputForKook processKookUserEvent(AxBotUserInput input) {
        AxBotUserInputForKook in = ((AxBotUserInputForKook) input);
        AxBotUserOutputForKook out = new AxBotUserOutputForKook();

        String userId = input.getFromUserId();
        String command = input.getRequestCommand();
        Long inputId = input.getInputId();

        out.setReplayToUser(userId);
        out.setReplayToMsg(in.getFromMsgId());
        out.setToChannel(in.getFromChannel());

        String[] cList = StringUtils.split(command);

        Optional<KookUserSetting> optKus = kookUserSettingService.findByUserId(userId);
        if (optKus.isEmpty()) {
            kookUserSettingService.newSetting(userId);
        } else {
            // 用户已被封禁
            if (optKus.get().getBanned()) {
                out.setContent(axBotHandlerForKook.userBanned(optKus.get().getBannedReason()));
                return out;
            }
        }

        boolean textPass = textCensorService.isTextPassCheck(command);
        boolean isWarning = false;
        long leftTimes = 0;
        if (!textPass) {
            userInputRecordService.updateSensitive(inputId, true);
            long times = userInputRecordService.countUserKookSensitiveInput(userId);
            leftTimes = 3 - times;
            if (leftTimes > 0) {
                isWarning = true;
            }
        }

        if (!textPass) {
            if (isWarning) {
                out.setContent(axBotHandlerForKook.sensitiveInput(leftTimes));
            } else {
                String reason = "输入3次及以上的逆天内容";
                kookUserSettingService.banUser(userId, reason);
                out.setContent(axBotHandlerForKook.userBanned(reason));
            }
            return out;
        }

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
    }

    private AxBotUserOutputForCqhttp processCqhttpUserEvent(AxBotUserInput input) {
        AxBotUserInputForCqhttp in = ((AxBotUserInputForCqhttp) input);
        AxBotUserOutputForCqhttp out = new AxBotUserOutputForCqhttp();
        out.setToGroup(in.getFromGroup());
        out.setReplayToUser(in.getFromUserId());

        String userId = input.getFromUserId();
        String command = input.getRequestCommand();
        Long inputId = input.getInputId();

        // TODO: 暂时不做qq的输入校验
        // boolean textPass = textCensorService.isTextPassCheck(command);
        // boolean isWarning = false;
        // long leftTimes = 0;
        // if (!textPass) {
        //     userInputRecordService.updateSensitive(inputId, true);
        //     long times = userInputRecordService.countUserCqhttpSensitiveInput(userId);
        //     leftTimes = 3 - times;
        //     if (leftTimes > 0) {
        //         isWarning = true;
        //     }
        // }
        //
        // if (!textPass) {
        //     if (isWarning) {
        //         out.setContent(axBotHandlerForCqhttp.sensitiveInput(leftTimes));
        //     } else {
        //         out.setContent(axBotHandlerForCqhttp.userBanned("输入3次及以上的逆天内容"));
        //     }
        //     return out;
        // }

        if (StringUtils.isBlank(command)) {
            out.setContent(axBotHandlerForCqhttp.getDefault());
        } else {
            AxbotCommand jc = AxbotCommand.judgeCommand(command);
            if (jc == null) {
                out.setContent(axBotHandlerForCqhttp.commandNotFound(command));
            } else if (jc == AxbotCommand.COMMAND_LUCKY) {
                String s = userId + LocalDate.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_DATE);
                out.setContent(axBotHandlerForCqhttp.getTodayLucky(s.hashCode()));
            } else {
                out.setContent(axBotHandlerForCqhttp.commandNotFound(command));
            }
        }
        return out;
    }
}
