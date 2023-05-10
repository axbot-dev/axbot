package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.data.repository.KookUserSettingRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class KookUserSettingService {
    @Resource
    KookUserSettingRepository kookUserSettingRepository;

    public KookUserSetting newSetting(String userId) {
        KookUserSetting entity = KookUserSetting.defaultSetting(userId);
        return kookUserSettingRepository.save(entity);
    }

    public Optional<KookUserSetting> findByUserId(String userId) {
        return kookUserSettingRepository.findByUserId(userId);
    }

    public List<KookUserSetting> findAllByBanned(Boolean banned) {
        return kookUserSettingRepository.findAllByBanned(banned);
    }


    public void blockUser(String userId, String reason) {
        log.info("kook user [{}] get blocked, because [{}]", userId, reason);
        kookUserSettingRepository.updateBannedAndBannedReasonAndBannedTimeByUserId(true, reason, LocalDateTime.now(), userId);
    }

    public void unblockUser(String userId) {
        log.info("kook user [{}] get unblocked", userId);
        kookUserSettingRepository.updateBannedAndBannedReasonAndBannedTimeByUserId(false, null, null, userId);
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

    public boolean canUseAI(String userId) {
        Optional<KookUserSetting> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return false;
        }
        KookUserSetting entity = opt.get();
        KookUserSetting.Permit permit = entity.getPermit();
        if (permit == null) {
            return false;
        }
        return Boolean.TRUE.equals(permit.getCanUseAI());
    }

    public void setCanUseAI(String userId, boolean canUseAI) {
        Optional<KookUserSetting> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return;
        }
        KookUserSetting entity = opt.get();
        if (entity.getPermit() == null) {
            entity.setPermit(new KookUserSetting.Permit());
        }
        KookUserSetting.Permit permit = entity.getPermit();
        permit.setCanUseAI(canUseAI);
        kookUserSettingRepository.save(entity);
    }
}
