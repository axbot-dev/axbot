package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import com.github.axiangcoding.axbot.server.data.repository.QUserSettingRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
}
