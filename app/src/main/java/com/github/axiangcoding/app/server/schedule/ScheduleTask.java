package com.github.axiangcoding.app.server.schedule;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleTask {
    // TODO 更换为真实任务
    @Scheduled(cron = "0 0/1 * * * ?")
    @SchedulerLock(name = "test")
    public void test() {
        log.info("do test task");
    }
}
