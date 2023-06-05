package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.engine.v1.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.v1.SupportPlatform;
import com.github.axiangcoding.axbot.server.data.entity.UserInputRecord;
import com.github.axiangcoding.axbot.server.data.repository.UserInputRecordRepository;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserInputRecordService {
    @Resource
    private UserInputRecordRepository userInputRecordRepository;

    public long saveRecordFromCqhttp(String userId, InteractiveCommand command, String message, String groupId) {
        UserInputRecord entity = new UserInputRecord();
        entity.setUserId(userId);
        entity.setPlatform(SupportPlatform.CQHTTP.getLabel());
        entity.setCommand(command.name());
        entity.setInput(message);
        entity.setFromQGroup(groupId);
        userInputRecordRepository.save(entity);
        return entity.getId();
    }

    public long saveRecordFromKook(String userId, InteractiveCommand command, String message, String guildId, String channelId) {
        UserInputRecord entity = new UserInputRecord();
        entity.setUserId(userId);
        entity.setPlatform(SupportPlatform.KOOK.getLabel());
        entity.setCommand(command.name());
        entity.setInput(message);
        entity.setFromKookGuild(guildId);
        entity.setFromKookChannel(channelId);
        userInputRecordRepository.save(entity);
        return entity.getId();
    }

    public void updateSensitive(long id, boolean sensitive) {
        userInputRecordRepository.updateSensitiveById(sensitive, id);
    }

    public List<UserInputRecord> findByUserId(String userId) {
        return userInputRecordRepository.findByUserId(userId);
    }

    public long countLatestSensitiveInput(String userId, SupportPlatform platform) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusMonths(1);
        return userInputRecordRepository.countByUserIdAndPlatformAndSensitiveAndCreateTimeAfter(
                userId, platform.getLabel(), true, start
        );
    }

    public List<UserInputRecord> findWtQueryHistory(String userId, String platform) {
        return userInputRecordRepository.findByUserIdAndPlatformAndCommandOrderByCreateTimeDesc(
                userId, platform, InteractiveCommand.WT_QUERY_PROFILE.name(), Pageable.ofSize(10).first());
    }

}
