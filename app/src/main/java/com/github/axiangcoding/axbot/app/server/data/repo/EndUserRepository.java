package com.github.axiangcoding.axbot.app.server.data.repo;

import com.github.axiangcoding.axbot.app.server.data.entity.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EndUserRepository extends JpaRepository<EndUser, Long> {
    Optional<EndUser> findByUserIdAndPlatform(String userId, String platform);

    List<EndUser> findByPlatformAndIsSuperAdmin(String platform, Boolean isSuperAdmin);



}