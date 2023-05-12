package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.engine.v1.SupportPlatform;
import com.github.axiangcoding.axbot.server.data.entity.UserInputRecord;
import com.github.axiangcoding.axbot.server.data.repository.UserInputRecordRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInputRecordService {
    @Resource
    private UserInputRecordRepository userInputRecordRepository;

    @Resource
    KookUserSettingService kookUserSettingService;

    public long saveRecordFromCqhttp(String userId, String message, String groupId) {
        UserInputRecord entity = new UserInputRecord();
        entity.setUserId(userId);
        entity.setPlatform(SupportPlatform.PLATFORM_CQHTTP.getName());
        entity.setInput(message);
        entity.setFromQGroup(groupId);
        userInputRecordRepository.save(entity);
        return entity.getId();
    }

    public long saveRecordFromKook(String userId, String message, String guildId, String channelId) {
        UserInputRecord entity = new UserInputRecord();
        entity.setUserId(userId);
        entity.setPlatform(SupportPlatform.PLATFORM_KOOK.getName());
        entity.setInput(message);
        entity.setFromKookGuild(guildId);
        entity.setFromKookChannel(channelId);
        userInputRecordRepository.save(entity);
        kookUserSettingService.upsertUsage(userId);
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
                SupportPlatform.PLATFORM_KOOK.getName(), true);
    }

    public long countUserCqhttpSensitiveInput(String userId) {
        return userInputRecordRepository.countByUserIdAndPlatformAndSensitive(userId,
                SupportPlatform.PLATFORM_CQHTTP.getName(), true);
    }

}
