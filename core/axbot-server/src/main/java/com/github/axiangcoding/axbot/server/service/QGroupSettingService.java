package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.QGroupSetting;
import com.github.axiangcoding.axbot.server.data.repository.QGroupSettingRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void updateWhenJoin(String groupId) {
        QGroupSetting qGroupSetting = getOrDefault(groupId);
        qGroupSetting.setActive(true);
        qGroupSettingRepository.save(qGroupSetting);
    }

    public void updateWhenExit(String groupId) {
        QGroupSetting qGroupSetting = getOrDefault(groupId);
        qGroupSetting.setActive(false);
        qGroupSettingRepository.save(qGroupSetting);
    }

    public List<QGroupSetting> findByEnabledBiliLiveReminder() {
        return qGroupSettingRepository.findByFunctionSettingEnableBiliLiveReminder(true);
    }

    public List<QGroupSetting> findByEnableNewsReminder() {
        return qGroupSettingRepository.findByFunctionSettingEnableWtNewsReminder(true);
    }
}
