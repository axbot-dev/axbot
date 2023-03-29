package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.service.axbot.AxbotCommand;
import com.github.axiangcoding.axbot.server.service.axbot.ResponseCallback;
import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotInput;
import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotOutput;
import com.github.axiangcoding.axbot.server.service.axbot.entity.kook.AxBotInputForKook;
import com.github.axiangcoding.axbot.server.service.axbot.entity.kook.AxBotOutputForKook;
import com.github.axiangcoding.axbot.server.service.axbot.handler.AxBotHandlerForKook;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class AxBotService {
    public static final Integer PLATFORM_KOOK = 1;
    public static final Integer PLATFORM_QQ = 2;


    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    AxBotHandlerForKook axBotHandlerForKook;

    /**
     * 同步响应
     *
     * @param input
     * @return
     */
    public AxBotOutput generateResponse(int replyPlatform, AxBotInput input) {
        if (replyPlatform == PLATFORM_KOOK) {
            AxBotInputForKook in = ((AxBotInputForKook) input);
            AxBotOutputForKook out = new AxBotOutputForKook();
            String userId = in.getRequestUser();
            out.setReplayToUser(userId);
            out.setReplayToMsg(in.getRequestMsgId());
            out.setReplayToChannel(in.getRequestChannel());

            String command = in.getRequestCommand();
            String[] commandList = StringUtils.split(command);

            if (commandList == null || commandList.length == 0) {
                out.setContent(axBotHandlerForKook.getDefault());
                return out;
            }

            String c0 = commandList[0];
            if (AxbotCommand.COMMAND_HELP.getTexts().contains(c0)) {
                out.setContent(axBotHandlerForKook.getHelp());
            } else if (AxbotCommand.COMMAND_VERSION.getTexts().contains(c0)) {
                out.setContent(axBotHandlerForKook.getVersion());
            } else if (AxbotCommand.COMMAND_LUCKY.getTexts().contains(c0)) {
                String s = userId + LocalDate.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_DATE);
                out.setContent(axBotHandlerForKook.getTodayLucky(s.hashCode()));
            } else {
                out.setContent(axBotHandlerForKook.notMatch(command));
            }
            return out;
        } else if (replyPlatform == PLATFORM_QQ) {
            // TODO
        }
        return null;
    }

    /**
     * 异步响应
     *
     * @param input
     * @param callback
     */
    public void generateResponseAsync(int replyPlatform, AxBotInput input, ResponseCallback callback) {
        threadPoolTaskExecutor.execute(() -> {
            AxBotOutput output = generateResponse(replyPlatform, input);
            callback.callback(output);
        });
    }
}
