package com.github.axiangcoding.axbot.app.server.data.repo;

import com.github.axiangcoding.axbot.app.server.data.entity.EndGuild;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EndGuildRepository extends JpaRepository<EndGuild, Long> {
    Optional<EndGuild> findByGuildIdAndPlatform(String guildId, String platform);


}