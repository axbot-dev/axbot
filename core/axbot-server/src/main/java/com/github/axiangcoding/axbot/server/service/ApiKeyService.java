package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.ApiKey;
import com.github.axiangcoding.axbot.server.data.repository.ApiKeyRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyService {
    @Resource
    ApiKeyRepository apiKeyRepository;

    public String getApiKeyFromRequest(HttpServletRequest request) {
        return request.getHeader("api-key");
    }

    public boolean isApiKeyValid(String key) {
        Optional<ApiKey> opt = apiKeyRepository.findByKey(key);
        if (opt.isEmpty()) {
            return false;
        }
        ApiKey apiKey = opt.get();
        if (apiKey.getNeverExpire()) {
            return true;
        }
        LocalDateTime expireTime = apiKey.getExpireTime();
        return expireTime.isAfter(LocalDateTime.now());
    }

    public boolean deleteByKey(String key) {
        long deleted = apiKeyRepository.deleteByKey(key);
        return deleted == 1;
    }

    public String generateApiKey(String userId, boolean neverExpire, long expire) {
        ApiKey apiKey = new ApiKey();
        UUID key = UUID.randomUUID();
        apiKey.setKey(key.toString());
        apiKey.setCreator(UUID.fromString(userId));
        if (neverExpire) {
            apiKey.setNeverExpire(true);
        } else {
            apiKey.setNeverExpire(false);
            apiKey.setExpireTime(LocalDateTime.now().plusSeconds(expire));
        }
        apiKeyRepository.save(apiKey);
        return key.toString();
    }
}
