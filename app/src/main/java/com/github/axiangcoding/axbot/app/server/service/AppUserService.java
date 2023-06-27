package com.github.axiangcoding.axbot.app.server.service;

import com.github.axiangcoding.axbot.app.server.data.entity.AppUser;
import com.github.axiangcoding.axbot.app.server.data.repo.AppUserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AppUserService {
    @Resource
    AppUserRepository repository;

    public void initSuperAdminUser() {
        String username = "admin";
        String password = "axbot";
        log.warn("Set the default superuser account, username: [{}], password: [{}]. " +
                        "For security reasons, " +
                        "you should always update the administrator password after the initial project",
                username, password);
        AppUser entity = new AppUser()
                .setUserId(UUID.randomUUID().toString())
                .setUsername(username)
                .setPassword(new BCryptPasswordEncoder().encode(password))
                .setStatus(AppUser.STATUS.NORMAL.name())
                .setIsSuperAdmin(true);
        repository.save(entity);
    }

    public long count() {
        return repository.count();
    }

    public boolean checkPassword(String username, String rawPassword) {
        Optional<AppUser> opt = repository.findByUsername(username);
        if (opt.isEmpty()) {
            log.warn("user [{}] does not exist", username);
            return false;
        } else {
            AppUser user = opt.get();
            boolean matches = new BCryptPasswordEncoder().matches(rawPassword, user.getPassword());
            LocalDateTime now = LocalDateTime.now();
            if (matches) {
                user.setLastLoginTime(now);
                user.setLoginFailed(0);
            } else {
                log.warn("user [{}] password error", username);
                Integer loginFailed = user.getLoginFailed();
                user.setLoginFailed(loginFailed == null ? 1 : loginFailed + 1);
                user.setLastLoginFailedTime(now);
            }
            repository.save(user);
            return matches;
        }
    }

    public Optional<AppUser> findByUsername(String username) {
        return repository.findByUsername(username);
    }
}
