package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.entity.GlobalUser;
import com.github.axiangcoding.axbot.server.data.repository.GlobalUserRepository;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class GlobalUserService {
    @Resource
    GlobalUserRepository globalUserRepository;


    public boolean checkPwd(String username, String password) {
        Optional<GlobalUser> opt = findByUsername(username);
        if (opt.isEmpty()) {
            return false;
        }
        GlobalUser entity = opt.get();
        boolean matches = new BCryptPasswordEncoder().matches(password, entity.getPassword());
        if (matches) {
            entity.setLastLoginTime(LocalDateTime.now());
            globalUserRepository.save(entity);
        }
        return matches;
    }

    public Optional<GlobalUser> findByUsername(String username) {
        return globalUserRepository.findByUsername(username);
    }

    public Optional<GlobalUser> findByUserId(String userId) {
        return globalUserRepository.findByUserId(UUID.fromString(userId));
    }

    public GlobalUser registerUser(String username, String rawPassword) {
        GlobalUser user = new GlobalUser();
        user.setUsername(username);
        user.setUserId(UUID.randomUUID());
        user.setPassword(new BCryptPasswordEncoder().encode(rawPassword));
        return globalUserRepository.save(user);
    }

    public boolean updatePassword(String userId, String oldPwd, String newPwd) {
        Optional<GlobalUser> opt = findByUserId(userId);
        if (opt.isEmpty()) {
            return false;
        }
        GlobalUser user = opt.get();
        boolean checkPwd = checkPwd(user.getUsername(), oldPwd);
        if (!checkPwd) {
            return false;
        }
        user.setPassword(new BCryptPasswordEncoder().encode(newPwd));
        globalUserRepository.save(user);
        return true;
    }

}
