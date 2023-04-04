package com.github.axiangcoding.axbot.engine;


import com.github.axiangcoding.axbot.engine.entity.AxBotUserOutput;

/**
 * 定义机器人的命令解析器
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

    /**
     * 获取版本
     *
     * @return
     */
    String getVersion();

    /**
     * 获取当天气运
     *
     * @param seed
     * @return
     */
    String getTodayLucky(long seed);

    String notMatch(String unknownCommand);

    String queryWTProfile(String nickname, AxBotUserOutput out);

    String updateWTProfile(String nickname, AxBotUserOutput out);

    String getGuildStatus(String guildId);

    String joinGuild(String guildId);

    void exitGuild(String guildId);
}
