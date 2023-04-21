package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.QGroupSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QGroupSettingRepository extends JpaRepository<QGroupSetting, Long> {
}