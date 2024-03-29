package com.github.axiangcoding.axbot.app.server.data.repo;

import com.github.axiangcoding.axbot.app.server.data.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUserId(String userId);
    Optional<AppUser> findByUsername(String username);

}