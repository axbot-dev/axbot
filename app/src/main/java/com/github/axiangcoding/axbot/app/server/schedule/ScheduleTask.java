package com.github.axiangcoding.axbot.app.server.schedule;

import com.github.axiangcoding.axbot.app.server.service.EndGuildService;
import com.github.axiangcoding.axbot.app.server.service.EndUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleTask {

    @Resource
    EndUserService endUserService;

    @Resource
    EndGuildService endGuildService;


    @Scheduled(cron = "@daily")
    @SchedulerLock(name = "cleanUsage")
    public void cleanUsage() {
        log.info("reset usage");
        endGuildService.resetTodayUsage();
        endUserService.resetTodayUsage();
    }
}
