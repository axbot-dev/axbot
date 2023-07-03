package com.github.axiangcoding.axbot.app.server.data.repo;

import com.github.axiangcoding.axbot.app.server.data.entity.EndUserInputRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EndUserInputRecordRepository extends JpaRepository<EndUserInputRecord, Long> {
    long countByUserIdAndPlatformAndIsSensitiveAndCreateTimeAfter(String userId, String platform,
                                                                  Boolean isSensitive, LocalDateTime start);
}