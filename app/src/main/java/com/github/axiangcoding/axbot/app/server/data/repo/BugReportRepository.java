package com.github.axiangcoding.axbot.app.server.data.repo;

import com.github.axiangcoding.axbot.app.server.data.entity.BugReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BugReportRepository extends JpaRepository<BugReport, Long> {
    Optional<BugReport> findByTraceId(String traceId);

}