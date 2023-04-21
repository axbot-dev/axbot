package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QUserSettingRepository extends JpaRepository<QUserSetting, Long> {
}