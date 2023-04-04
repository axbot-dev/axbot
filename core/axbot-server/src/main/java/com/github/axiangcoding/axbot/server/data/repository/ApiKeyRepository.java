package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    long deleteByKey(String key);

    Optional<ApiKey> findByKey(String key);

}