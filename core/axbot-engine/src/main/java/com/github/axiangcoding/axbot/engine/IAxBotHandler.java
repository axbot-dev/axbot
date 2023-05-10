package com.github.axiangcoding.axbot.engine;


import com.github.axiangcoding.axbot.engine.entity.AxBotUserOutput;

/**
 * 定义机器人的命令解析器
 */
public interface IAxBotHandler {
    /**
     * 默认消息
     *
     * @return 响应消息
     */
    String getDefault();

    /**
     * 敏感输入
     *
     * @return 响应消息
     */
    String sensitiveInput(long left);

    /**
     * 达到社群输入上限
     *
     * @param usage
     * @param limit
     * @return 响应消息
     */
    String reachedGuildLimit(int usage, int limit);

    /**
     * 达到个人输入上限
     *
     * @param usage 使用量
     * @param limit 限制量
     * @return 响应消息
     */
    String reachedUserLimit(int usage, int limit);

    /**
     * 用户被拉黑
     *
     * @param reason 原因
     * @return 响应消息
     */
    String userBanned(String reason);

    /**
     * 社群被拉黑
     *
     * @param reason 原因
     * @return
     */
    String guildBanned(String reason);

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

    String chatWithAI(String userId, String ask);

    String joinGuild(String guildId);

    void exitGuild(String guildId);

    String biliLiveRemind(Long roomId, String title, String areaName, String description);

    String sendWtNew(String url, String title, String comment, String posterUrl, String dateStr);

}
