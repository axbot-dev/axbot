package com.github.axiangcoding.axbot.engine;

import com.github.axiangcoding.axbot.engine.entity.AxBotInput;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotInputForKook;
import com.github.axiangcoding.axbot.engine.entity.AxBotOutput;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotOutputForKook;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class AxBotEngine {
    public static final Integer PLATFORM_KOOK = 1;
    public static final Integer PLATFORM_QQ = 2;

    private final ThreadPoolExecutor executor;

    public AxBotEngine(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    /**
     * 同步响应
     *
     * @param input
     * @return
     */
    public AxBotOutput generateResponse(int replyPlatform, AxBotInput input) {
        if (replyPlatform == PLATFORM_KOOK) {
            AxBotInputForKook in = ((AxBotInputForKook) input);
            AxBotOutputForKook output = new AxBotOutputForKook();
            // TODO 解析命令，处理返回值
            output.setContent("**hello world**");
            output.setReplayToUser(in.getRequestUser());
            output.setReplayToMsg(in.getRequestMsgId());
            output.setReplayToChannel(in.getRequestChannel());
            return output;
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
        executor.execute(() -> {
            AxBotOutput output = generateResponse(replyPlatform, input);
            callback.callback(output);
        });
    }
}
