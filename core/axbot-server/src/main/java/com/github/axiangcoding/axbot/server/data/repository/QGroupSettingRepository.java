package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.QGroupSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QGroupSettingRepository extends JpaRepository<QGroupSetting, Long> {
    Optional<QGroupSetting> findByGroupId(String groupId);

    List<QGroupSetting> findByFunctionSettingEnableBiliLiveReminder(boolean enabledBiliLiveReminder);

    List<QGroupSetting> findByFunctionSettingEnableWtNewsReminder(boolean enabledWtNewsReminder);
}