package com.github.axiangcoding.axbot.app.server.data.repo;

import com.github.axiangcoding.axbot.app.server.data.entity.EndGuild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface EndGuildRepository extends JpaRepository<EndGuild, Long> {
    Optional<EndGuild> findByGuildIdAndPlatform(String guildId, String platform);

    @Transactional
    @Modifying
    @Query("update EndGuild u set u.usage.inputToday = 0, u.usage.queryWtToday = 0")
    int resetTodayUsage();
}