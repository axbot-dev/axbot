package com.github.axiangcoding.axbot.app.server.data.repo;

import com.github.axiangcoding.axbot.app.server.data.entity.EndUserInputRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndUserInputRecordRepository extends JpaRepository<EndUserInputRecord, Long> {
}