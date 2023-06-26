package com.github.axiangcoding.app.server.service;

import com.github.axiangcoding.app.server.data.entity.AppUser;
import com.github.axiangcoding.app.server.data.repo.AppUserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        AppUser entity = AppUser.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(new BCryptPasswordEncoder().encode(password))
                .isSuperAdmin(true)
                .build();
        repository.save(entity);
    }

    public long count() {
        return repository.count();
    }
}
