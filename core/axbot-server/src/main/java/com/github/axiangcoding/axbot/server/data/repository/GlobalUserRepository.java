package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.GlobalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GlobalUserRepository extends JpaRepository<GlobalUser, Long> {
    Optional<GlobalUser> findByUsername(String username);

    Optional<GlobalUser> findByUserId(UUID userId);
}