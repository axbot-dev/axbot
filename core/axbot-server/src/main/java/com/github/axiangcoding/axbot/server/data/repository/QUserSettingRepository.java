package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.QUserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface QUserSettingRepository extends JpaRepository<QUserSetting, Long> {
    Optional<QUserSetting> findByUserId(String userId);

    @Transactional
    @Modifying
    @Query("update QUserSetting q set q.banned = ?1, q.bannedReason = ?2, q.bannedTime = ?3 where q.userId = ?4")
    int updateBannedAndBannedReasonAndBannedTimeByUserId(Boolean banned, String bannedReason, LocalDateTime bannedTime, String userId);

    @Transactional
    @Modifying
    @Query("update QUserSetting q set q.usage.inputToday = 0, q.usage.queryWtToday = 0")
    int resetTodayUsage();
}