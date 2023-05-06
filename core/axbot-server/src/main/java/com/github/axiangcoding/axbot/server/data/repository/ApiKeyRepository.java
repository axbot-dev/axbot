package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.ApiKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    @Transactional
    @Modifying
    long deleteByKey(String key);

    Optional<ApiKey> findByKey(String key);

    List<ApiKey> findAllByCreator(UUID creator);
}