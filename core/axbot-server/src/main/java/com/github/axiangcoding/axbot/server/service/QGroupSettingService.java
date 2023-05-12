package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.QGroupSetting;
import com.github.axiangcoding.axbot.server.data.repository.QGroupSettingRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QGroupSettingService {
    @Resource
    QGroupSettingRepository qGroupSettingRepository;

    public Optional<QGroupSetting> findByGroupId(String groupId) {
        return qGroupSettingRepository.findByGroupId(groupId);
    }

    public QGroupSetting getOrDefault(String groupId){
        QGroupSetting qGroupSetting;
        Optional<QGroupSetting> opt = findByGroupId(groupId);
        qGroupSetting = opt.orElseGet(() -> qGroupSettingRepository.save(QGroupSetting.defaultSetting(groupId)));
        return qGroupSetting;
    }

}
