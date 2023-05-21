package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.SponsorOrder;
import com.github.axiangcoding.axbot.server.data.entity.basic.BindProfile;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserPermit;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserSubscribe;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserUsage;
import com.github.axiangcoding.axbot.server.data.repository.QUserSettingRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class QUserSettingService {
    @Resource
    QUserSettingRepository qUserSettingRepository;

    public Optional<QUserSetting> findByUserId(String userId) {
        return qUserSettingRepository.findByUserId(userId);
    }

    public QUserSetting getOrDefault(String userId) {
        QUserSetting qUserSetting;
        Optional<QUserSetting> opt = findByUserId(userId);
        qUserSetting = opt.orElseGet(() -> qUserSettingRepository.save(QUserSetting.defaultSetting(userId)));
        return qUserSetting;
    }

    public int getInputLimit(String userId) {
        if (isSubscribedBasicPlan(userId)) {
            return 200;
        } else {
            return 50;
        }
    }

    public boolean isSubscribedBasicPlan(String userId) {
        Optional<QUserSetting> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return false;
        }
        QUserSetting setting = opt.get();
        UserSubscribe subscribe = setting.getSubscribe();
        if (subscribe != null) {
            boolean b = SponsorOrder.PLAN.BASIC_PERSONAL.getName().equals(subscribe.getPlan());
            return b && subscribe.getExpireAt().isAfter(LocalDateTime.now());
        } else {
            return false;
        }
    }

    public void updateInputUsage(QUserSetting setting) {
        UserUsage usage = setting.getUsage();
        usage.setInputToday(usage.getInputToday() + 1);
        usage.setInputTotal(usage.getInputTotal() + 1);
        qUserSettingRepository.save(setting);
    }

    public void blockUser(String userId, String reason) {
        log.info("qq user [{}] get blocked, because [{}]", userId, reason);
        qUserSettingRepository.updateBannedAndBannedReasonAndBannedTimeByUserId(true, reason, LocalDateTime.now(), userId);
    }

    public void unblockUser(String userId) {
        log.info("qq user [{}] get unblocked", userId);
        qUserSettingRepository.updateBannedAndBannedReasonAndBannedTimeByUserId(false, null, null, userId);
    }

    public boolean canUseAI(String userId) {
        Optional<QUserSetting> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return false;
        }
        QUserSetting entity = opt.get();
        UserPermit permit = entity.getPermit();
        if (permit == null) {
            return false;
        }
        return Boolean.TRUE.equals(permit.getCanUseAIChat());
    }

    public void bindWtNickname(String userId, String nickname) {
        Optional<QUserSetting> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return;
        }
        QUserSetting setting = opt.get();
        BindProfile bindProfile = setting.getBindProfile();
        if (bindProfile == null) {
            bindProfile = new BindProfile();
            bindProfile.setWtNickname(nickname);
            setting.setBindProfile(bindProfile);
        } else {
            bindProfile.setWtNickname(nickname);
        }
        qUserSettingRepository.save(setting);
    }

    public void unbindWtNickname(String userId) {
        Optional<QUserSetting> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return;
        }
        QUserSetting setting = opt.get();
        BindProfile bindProfile = setting.getBindProfile();
        if (bindProfile == null) {
            return;
        } else {
            bindProfile.setWtNickname(null);
        }
        qUserSettingRepository.save(setting);
    }
}
