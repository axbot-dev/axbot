package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.QGroupSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QGroupSettingRepository extends JpaRepository<QGroupSetting, Long> {
    Optional<QGroupSetting> findByGroupId(String groupId);
}