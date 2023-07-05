package com.github.axiangcoding.axbot.app.server.data.repo;

import com.github.axiangcoding.axbot.app.server.data.entity.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface EndUserRepository extends JpaRepository<EndUser, Long> {
    Optional<EndUser> findByUserIdAndPlatform(String userId, String platform);

    List<EndUser> findByPlatformAndIsSuperAdmin(String platform, Boolean isSuperAdmin);


    @Transactional
    @Modifying
    @Query("update EndUser u set u.usage.inputToday = 0, u.usage.queryWtToday = 0")
    int resetTodayUsage();
}