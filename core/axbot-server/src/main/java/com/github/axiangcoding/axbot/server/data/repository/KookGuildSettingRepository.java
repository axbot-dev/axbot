package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface KookGuildSettingRepository extends JpaRepository<KookGuildSetting, Long> {
    Optional<KookGuildSetting> findByGuildId(String guildId);

    @Transactional
    @Modifying
    @Query("update KookGuildSetting k set k.active = ?1 where k.guildId = ?2")
    int updateActiveByGuildId(Boolean active, String guildId);

    List<KookGuildSetting> findByFunctionSettingEnableBiliLiveReminder(Boolean enableBiliLiveReminder);

    List<KookGuildSetting> findByFunctionSettingEnableWtNewsReminder(Boolean wtNewsReminder);
}