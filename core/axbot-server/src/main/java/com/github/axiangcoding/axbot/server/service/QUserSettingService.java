package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import com.github.axiangcoding.axbot.server.data.entity.SponsorOrder;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserSubscribe;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserUsage;
import com.github.axiangcoding.axbot.server.data.repository.QUserSettingRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
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

    public void updateInputUsage(String userId) {
        Optional<QUserSetting> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return;
        }
        QUserSetting setting = opt.get();
        if (setting.getUsage() == null) {
            setting.setUsage(UserUsage.defaultUsage());
        }
        UserUsage usage = setting.getUsage();
        usage.setInputToday(usage.getInputToday() + 1);
        usage.setInputTotal(usage.getInputTotal() + 1);
        qUserSettingRepository.save(setting);
    }
}
