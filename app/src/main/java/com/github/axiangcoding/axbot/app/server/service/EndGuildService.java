package com.github.axiangcoding.axbot.app.server.service;

import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.server.data.entity.EndGuild;
import com.github.axiangcoding.axbot.app.server.data.repo.EndGuildRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class EndGuildService {
    @Resource
    EndGuildRepository repository;

    public EndGuild getOrCreate(String guildId, BotPlatform platform) {
        String pn = platform.name();
        return repository.findByGuildIdAndPlatform(guildId, pn).orElseGet(() -> {
            EndGuild entity = EndGuild.init(guildId, pn);
            return repository.save(entity);
        });
    }
}
