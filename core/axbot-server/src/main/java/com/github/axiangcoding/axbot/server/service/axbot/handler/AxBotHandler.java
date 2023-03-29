package com.github.axiangcoding.axbot.server.service.axbot.handler;

/**
 * 定义机器人可以处理的行为
 */
public interface AxBotHandler {
    /**
     * 获取默认消息
     *
     * @return
     */
    String getDefault();

    /**
     * 获取帮助信息
     *
     * @return
     */
    String getHelp();


    String getVersion();

    String getTodayLucky(long seed);

    String notMatch(String unknownCommand);
}
