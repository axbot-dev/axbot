package com.github.axiangcoding.axbot.engine;

public interface IAxBotHandlerForKook extends IAxBotHandler {
    /**
     * 管理群组
     *
     * @param userId
     * @param guildId
     * @param channelId
     * @param command
     * @return
     */
    String manageGuild(String userId, String guildId, String channelId, String command);
}
