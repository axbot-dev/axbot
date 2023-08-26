package com.github.axiangcoding.axbot.app.server.service;

import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.enums.FunctionType;
import com.github.axiangcoding.axbot.app.server.data.entity.EndUser;
import com.github.axiangcoding.axbot.app.server.data.entity.EndUserInputRecord;
import com.github.axiangcoding.axbot.app.server.data.entity.field.EndUserStatus;
import com.github.axiangcoding.axbot.app.server.data.entity.field.EndUserUsage;
import com.github.axiangcoding.axbot.app.server.data.repo.EndUserInputRecordRepository;
import com.github.axiangcoding.axbot.app.server.data.repo.EndUserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EndUserService {
    @Resource
    EndUserRepository repository;

    @Resource
    EndUserInputRecordRepository endUserInputRecordRepository;

    public EndUser getOrCreate(String authorId, BotPlatform platform) {
        String pn = platform.name();
        return repository.findByUserIdAndPlatform(authorId, pn).orElseGet(() -> {
            EndUser entity = EndUser.init(authorId, pn);
            return repository.save(entity);
        });
    }

    public long recordInput(BotPlatform platform, String guildId, String channelId,
                            String authorId, String input, FunctionType cmd) {
        EndUserInputRecord entity = new EndUserInputRecord()
                .setUserId(authorId)
                .setPlatform(platform.name())
                .setInput(input)
                .setCommand(cmd.name())
                .setGuildId(guildId)
                .setChannelId(channelId)
                .setIsSensitive(false);
        endUserInputRecordRepository.save(entity);
        return entity.getId();
    }

    public void markInputSensitive(long id) {
        endUserInputRecordRepository.findById(id).ifPresent(entity -> {
            entity.setIsSensitive(true);
            endUserInputRecordRepository.save(entity);
        });
    }

    public void setSuperAdmin(BotPlatform platform, String userId, Boolean isSuperAdmin) {
        repository.findByUserIdAndPlatform(userId, platform.name()).ifPresent(entity -> {
            entity.setIsSuperAdmin(isSuperAdmin);
            repository.save(entity);
        });
    }

    public List<EndUser> getSuperAdmins(BotPlatform platform) {
        return repository.findByPlatformAndIsSuperAdmin(platform.name(), true);
    }

    /**
     * 获取用户的输入限制
     *
     * @param userId
     * @param platform
     * @return
     */
    public int getInputLimit(String userId, BotPlatform platform) {
        // 目前固定为单日20
        return 20;
    }

    /**
     * 获取用户的请求战雷的限制
     *
     * @param userId
     * @param platform
     * @return
     */
    public int getQueryWtLimit(String userId, BotPlatform platform) {
        // 目前固定为单日5
        return 5;
    }

    public long countLatestSensitiveInput(String userId, BotPlatform platform) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusMonths(1);
        return endUserInputRecordRepository.countByUserIdAndPlatformAndIsSensitiveAndCreateTimeAfter(
                userId, platform.name(), true, start
        );
    }

    public boolean isBlocked(EndUser endUser) {
        EndUserStatus status = endUser.getStatus();
        if (EndUserStatus.STATUS.LOCKED.name().equals(status.getStatus())) {
            if (status.getBannedUntil().isAfter(LocalDateTime.now())) {
                return true;
            } else {
                // 解封
                endUser.setStatus(EndUserStatus.normal());
                repository.save(endUser);
            }
        }
        return false;
    }

    public void blockUser(String userId, BotPlatform platform, String reason) {
        repository.findByUserIdAndPlatform(userId, platform.name()).ifPresent(entity -> {
            EndUserStatus status = entity.getStatus();
            status.setStatus(EndUserStatus.STATUS.LOCKED.name());
            status.setBannedReason(reason);
            status.setBannedAt(LocalDateTime.now());
            // 封禁7天
            status.setBannedUntil(LocalDateTime.now().plusDays(7));
            repository.save(entity);
        });
    }

    public void updateUsage(EndUser endUser) {
        EndUserUsage usage = endUser.getUsage();
        usage.setInputToday(usage.getInputToday() + 1);
        usage.setInputTotal(usage.getInputTotal() + 1);
        repository.save(endUser);
    }

    public void resetTodayUsage() {
        repository.resetTodayUsage();
    }
}
