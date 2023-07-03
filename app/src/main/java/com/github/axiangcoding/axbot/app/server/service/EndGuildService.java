package com.github.axiangcoding.axbot.app.server.service;

import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.server.data.entity.EndGuild;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.EndGuildStatus;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.EndGuildUsage;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.EndUserStatus;
import com.github.axiangcoding.axbot.app.server.data.repo.EndGuildRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    /**
     * 获取频道的输入限制
     *
     * @param guildId
     * @param platform
     * @return
     */
    public int getInputLimit(String guildId, BotPlatform platform) {
        return 200;
    }

    /**
     * 获取频道的请求战雷的限制
     *
     * @param guildId
     * @param platform
     * @return
     */
    public int getQueryWtLimit(String guildId, BotPlatform platform) {
        return 20;
    }

    public boolean isBlocked(EndGuild endGuild) {
        EndGuildStatus status = endGuild.getStatus();
        if (EndGuildStatus.STATUS.LOCKED.name().equals(status.getStatus())) {
            if (status.getBannedUntil().isAfter(LocalDateTime.now())) {
                return true;
            } else {
                // 解封
                endGuild.setStatus(EndGuildStatus.normal());
                repository.save(endGuild);
            }
        }
        return false;
    }

    public void blockGuild(String guildId, BotPlatform platform, String reason) {
        repository.findByGuildIdAndPlatform(guildId, platform.name()).ifPresent(entity -> {
            EndGuildStatus status = entity.getStatus();
            status.setStatus(EndUserStatus.STATUS.LOCKED.name());
            status.setBannedReason(reason);
            status.setBannedAt(LocalDateTime.now());
            // 封禁7天
            status.setBannedUntil(LocalDateTime.now().plusDays(7));
            repository.save(entity);
        });
    }

    public void updateUsage(EndGuild endGuild) {
        EndGuildUsage usage = endGuild.getUsage();
        usage.setInputToday(usage.getInputToday() + 1);
        usage.setInputTotal(usage.getInputTotal() + 1);
        repository.save(endGuild);
    }

    public void resetTodayUsage() {
        repository.resetTodayUsage();
    }
}
