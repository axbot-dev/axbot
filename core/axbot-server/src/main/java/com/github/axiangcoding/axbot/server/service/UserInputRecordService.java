package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.engine.v1.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.v1.SupportPlatform;
import com.github.axiangcoding.axbot.server.data.entity.UserInputRecord;
import com.github.axiangcoding.axbot.server.data.repository.UserInputRecordRepository;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInputRecordService {
    @Resource
    private UserInputRecordRepository userInputRecordRepository;

    public long saveRecordFromCqhttp(String userId, InteractiveCommand command, String message, String groupId) {
        UserInputRecord entity = new UserInputRecord();
        entity.setUserId(userId);
        entity.setPlatform(SupportPlatform.PLATFORM_CQHTTP.getLabel());
        entity.setCommand(command.name());
        entity.setInput(message);
        entity.setFromQGroup(groupId);
        userInputRecordRepository.save(entity);
        return entity.getId();
    }

    public long saveRecordFromKook(String userId, InteractiveCommand command, String message, String guildId, String channelId) {
        UserInputRecord entity = new UserInputRecord();
        entity.setUserId(userId);
        entity.setPlatform(SupportPlatform.PLATFORM_KOOK.getLabel());
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


    public long countUserKookSensitiveInput(String userId) {
        return userInputRecordRepository.countByUserIdAndPlatformAndSensitive(userId,
                SupportPlatform.PLATFORM_KOOK.getLabel(), true);
    }

    public long countUserCqhttpSensitiveInput(String userId) {
        return userInputRecordRepository.countByUserIdAndPlatformAndSensitive(userId,
                SupportPlatform.PLATFORM_CQHTTP.getLabel(), true);
    }

    public List<UserInputRecord> findWtQueryHistory(String userId, String platform) {
        return userInputRecordRepository.findByUserIdAndPlatformAndCommandOrderByCreateTimeDesc(
                userId, platform, InteractiveCommand.WT_QUERY_PROFILE.name(), Pageable.ofSize(10).first());
    }

}
