package com.github.axiangcoding.axbot.app.server.schedule;

import com.github.axiangcoding.axbot.app.bot.function.FunctionHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleTask {

    @Resource
    FunctionHandler handler;

    // TODO 更换为真实任务
    @Scheduled(cron = "0 0/1 * * * ?")
    @SchedulerLock(name = "test")
    public void test() {
        // handler.triggerEvent(ActiveEvent.TEST, BotPlatform.KOOK, new HashMap<>());
        log.info("do test task");
    }
}
