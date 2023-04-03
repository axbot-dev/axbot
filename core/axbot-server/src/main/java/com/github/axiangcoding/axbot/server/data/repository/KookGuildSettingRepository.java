package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KookGuildSettingRepository extends JpaRepository<KookGuildSetting, Long> {
    Optional<KookGuildSetting> findByGuildId(String guildId);
}