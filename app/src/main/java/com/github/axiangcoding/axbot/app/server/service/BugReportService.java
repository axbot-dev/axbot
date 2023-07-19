package com.github.axiangcoding.axbot.app.server.service;

import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.server.data.entity.BugReport;
import com.github.axiangcoding.axbot.app.server.data.repo.BugReportRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BugReportService {
    @Resource
    BugReportRepository repository;

    public void reportTrace(BotPlatform platform, String userId, String traceId) {
        repository.save(new BugReport()
                .setUserId(userId)
                .setPlatform(platform.name())
                .setTraceId(traceId)
        );
    }

    public void reportBug(BotPlatform platform, String userId, String detail) {
        repository.save(new BugReport()
                .setUserId(userId)
                .setPlatform(platform.name())
                .setContent(detail)
        );
    }

    public boolean traceBugExist(String traceId) {
        return repository.findByTraceId(traceId).isPresent();
    }
}
