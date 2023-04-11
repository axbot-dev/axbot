package com.github.axiangcoding.axbot.server.schedule;

import com.github.axiangcoding.axbot.server.service.BotKookService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
public class ScheduleTask {

    @Resource
    BotKookService botKookService;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void checkBiliRoom() {
        // TODO 确保并发正常
        log.info("start regularly checking the status of bili streaming");
        botKookService.checkBiliRoomStatus();
    }

    @Scheduled(cron = "@daily")
    public void cleanUsage() {
        // TODO 确保并发正常
        log.info("clean usage daily");
    }


}
