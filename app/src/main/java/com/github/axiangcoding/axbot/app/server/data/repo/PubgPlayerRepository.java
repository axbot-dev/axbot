package com.github.axiangcoding.axbot.app.server.data.repo;

import com.github.axiangcoding.axbot.app.server.data.entity.PubgPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PubgPlayerRepository extends JpaRepository<PubgPlayer, Long> {
    Optional<PubgPlayer> findByPlayerId(String playerId);

    Optional<PubgPlayer> findByPlayerName(String playerName);

}