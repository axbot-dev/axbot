package com.github.axiangcoding.axbot.app.server.service;

import com.github.axiangcoding.axbot.app.server.data.repo.ApiKeyRepository;
import com.github.axiangcoding.axbot.app.server.data.entity.ApiKey;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ApiKeyService {
    @Resource
    ApiKeyRepository repository;

    public List<ApiKey> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    public String generateKey(String userId, String description, boolean neverExpire, long expireSeconds) {
        String key = UUID.randomUUID().toString();
        ApiKey apiKey = new ApiKey()
                .setVal(key)
                .setUserId(userId)
                .setDescription(description);
        if (neverExpire) {
            apiKey.setNeverExpire(true);
        } else {
            apiKey.setNeverExpire(false);
            apiKey.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
        }
        repository.save(apiKey);
        return key;
    }

    public boolean expireKey(String apiKey) {
        return repository.deleteByVal(apiKey) > 0;
    }
}
