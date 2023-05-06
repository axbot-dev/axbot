package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.ApiKey;
import com.github.axiangcoding.axbot.server.data.entity.GlobalUser;
import com.github.axiangcoding.axbot.server.data.repository.ApiKeyRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyService {
    @Resource
    ApiKeyRepository apiKeyRepository;

    @Resource
    GlobalUserService globalUserService;

    public String getApiKeyFromRequest(HttpServletRequest request) {
        return request.getHeader("api-key");
    }

    public List<ApiKey> findAllByCreator(String userId) {
        return apiKeyRepository.findAllByCreator(UUID.fromString(userId));
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


    public Optional<ApiKey> findByKey(String key) {
        return apiKeyRepository.findByKey(key);
    }

    public Optional<GlobalUser> findUserByKey(String key){
        Optional<ApiKey> opt = findByKey(key);
        if(opt.isEmpty()){
            return Optional.empty();
        }
        return globalUserService.findByUserId(opt.get().getCreator().toString());
    }

    public boolean deleteByKey(String key) {
        long deleted = apiKeyRepository.deleteByKey(key);
        return deleted == 1;
    }

    public String generateApiKey(String userId, String comment, boolean neverExpire, long expire) {
        ApiKey apiKey = new ApiKey();
        UUID key = UUID.randomUUID();
        apiKey.setKey(key.toString());
        apiKey.setCreator(UUID.fromString(userId));
        apiKey.setComment(comment);
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
