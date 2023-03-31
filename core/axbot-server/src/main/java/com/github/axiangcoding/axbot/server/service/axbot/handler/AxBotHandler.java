package com.github.axiangcoding.axbot.server.service.axbot.handler;

import com.github.axiangcoding.axbot.server.service.axbot.entity.AxBotOutput;

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

    String queryWTProfile(String nickname, AxBotOutput out);

    String updateWTProfile(String nickname, AxBotOutput out);
}
