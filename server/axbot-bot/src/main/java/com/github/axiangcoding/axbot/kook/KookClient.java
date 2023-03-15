package com.github.axiangcoding.axbot.kook;

import com.github.axiangcoding.axbot.kook.service.GuildService;

public class KookClient {
    private final GuildService guildService;

    public KookClient(String botToken) {
        if (!botToken.startsWith("Bot")) {
            botToken = "Bot " + botToken;
        }
        this.guildService = KookServiceGenerator.createService(GuildService.class, botToken);
    }

    public GuildService guildService() {
        return guildService;
    }

}
