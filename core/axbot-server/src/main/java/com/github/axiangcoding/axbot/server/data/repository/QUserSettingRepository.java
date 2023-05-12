package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QUserSettingRepository extends JpaRepository<QUserSetting, Long> {
    Optional<QUserSetting> findByUserId(String userId);
}