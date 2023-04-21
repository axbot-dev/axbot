package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface KookUserSettingRepository extends JpaRepository<KookUserSetting, Long> {
    @Transactional
    @Modifying
    @Query("update KookUserSetting k set k.banned = ?1, k.bannedReason = ?2, k.bannedTime = ?3 where k.userId = ?3")
    int updateBannedAndBannedReasonAndBannedTimeByUserId(Boolean banned, String bannedReason, LocalDateTime bannedTime, String userId);

    Optional<KookUserSetting> findByUserId(String userId);

}