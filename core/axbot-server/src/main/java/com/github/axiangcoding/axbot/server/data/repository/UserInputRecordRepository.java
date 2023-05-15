package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.UserInputRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserInputRecordRepository extends JpaRepository<UserInputRecord, Long> {
    @Transactional
    @Modifying
    @Query("update UserInputRecord u set u.sensitive = ?1 where u.id = ?2")
    int updateSensitiveById(Boolean sensitive, Long id);

    List<UserInputRecord> findByUserId(String userId);

    long countByUserIdAndPlatformAndSensitive(String userId, String platform, Boolean sensitive);

    List<UserInputRecord> findByUserIdAndPlatformAndCommandOrderByCreateTimeDesc(String userId,
                                                                                 String platform,
                                                                                 String command,
                                                                                 Pageable pageable);


}