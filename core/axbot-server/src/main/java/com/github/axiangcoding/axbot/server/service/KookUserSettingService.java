package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.repository.KookUserSettingRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class KookUserSettingService {
    @Resource
    KookUserSettingRepository kookUserSettingRepository;

    public KookUserSetting newSetting(String userId) {
        KookUserSetting entity = new KookUserSetting();
        entity.setUserId(userId);
        entity.setBanned(false);
        entity.getUsage().setInputToday(0);
        entity.getUsage().setInputTotal(0L);
        entity.getUsage().setQueryWtToday(0);
        entity.getUsage().setQueryWtTotal(0L);
        return kookUserSettingRepository.save(entity);
    }

    public Optional<KookUserSetting> findByUserId(String userId) {
        return kookUserSettingRepository.findByUserId(userId);
    }

    public void banUser(String userId, String reason) {
        log.info("kook user [{}] get banned, because [{}]", userId, reason);
        kookUserSettingRepository.updateBannedAndBannedReasonAndBannedTimeByUserId(true, reason, LocalDateTime.now(), userId);
    }

    public void upsertUsage(String userId) {
        Optional<KookUserSetting> opt = kookUserSettingRepository.findByUserId(userId);
        KookUserSetting kookUserSetting;
        kookUserSetting = opt.orElseGet(() -> newSetting(userId));

        KookUserSetting.Usage usage = kookUserSetting.getUsage();
        usage.setInputToday(usage.getInputToday() + 1);
        usage.setInputTotal(usage.getInputTotal() + 1);
        kookUserSettingRepository.save(kookUserSetting);
    }

    public void resetTodayUsage() {
        kookUserSettingRepository.resetTodayUsage();
    }
}
