package com.github.axiangcoding.axbot.engine;


import com.github.axiangcoding.axbot.engine.entity.AxBotUserOutput;

/**
 * 定义机器人的命令解析器
 */
public interface IAxBotHandler {
    /**
     * 获取默认消息
     *
     * @return 响应消息
     */
    String getDefault();

    /**
     * 获取帮助信息
     *
     * @return 响应消息
     */
    String getHelp();

    /**
     * 获取版本
     *
     * @return 响应消息
     */
    String getVersion();

    /**
     * 获取当天气运
     *
     * @param seed 气运种子
     * @return 响应消息
     */
    String getTodayLucky(long seed);

    /**
     * 未匹配到命令
     *
     * @param unknownCmd 未知命令
     * @return 响应消息
     */
    String commandNotFound(String unknownCmd);


    String queryWTProfile(String nickname, AxBotUserOutput out);

    String updateWTProfile(String nickname, AxBotUserOutput out);

    String getGuildStatus(String guildId);

    String joinGuild(String guildId);

    void exitGuild(String guildId);


}
