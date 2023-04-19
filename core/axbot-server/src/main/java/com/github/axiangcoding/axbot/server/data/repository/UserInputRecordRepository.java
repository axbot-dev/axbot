package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.UserInputRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInputRecordRepository extends JpaRepository<UserInputRecord, Long> {
}