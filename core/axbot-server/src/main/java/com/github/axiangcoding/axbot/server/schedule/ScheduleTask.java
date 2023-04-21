package com.github.axiangcoding.axbot.server.schedule;

import com.github.axiangcoding.axbot.engine.entity.AxBotSupportPlatform;
import com.github.axiangcoding.axbot.server.cache.CacheKeyGenerator;
import com.github.axiangcoding.axbot.server.service.AxBotService;
import com.github.axiangcoding.axbot.server.service.BotKookService;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
public class ScheduleTask {

    @Resource
    BotKookService botKookService;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    AxBotService axBotService;

    @Resource
    KookUserSettingService kookUserSettingService;

    private static final String LOCK_CHECK_BILI_ROOM_KEY = CacheKeyGenerator.getCronJobLockKey("checkBiliRoom");
    private static final String LOCK_RESET_USAGE = CacheKeyGenerator.getCronJobLockKey("resetUsage");

    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkBiliRoom() {
        if (!axBotService.isPlatformEnabled(AxBotSupportPlatform.PLATFORM_KOOK)) {
            return;
        }

        boolean lock = false;
        try {
            // 尝试获取锁
            lock = Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(LOCK_CHECK_BILI_ROOM_KEY, "locked"));
            if (lock) {
                log.info("get lock, start regularly checking the status of bili streaming");
                botKookService.checkBiliRoomStatus();
            } else {
                // 获取锁失败，放弃任务执行
                log.warn("failed to get lock, abort checking the status of bili streaming");
            }
        } finally {
            // 释放锁
            if (lock) {
                stringRedisTemplate.delete(LOCK_CHECK_BILI_ROOM_KEY);
            }
        }
    }

    @Scheduled(cron = "@daily")
    public void cleanUsage() {
        boolean lock = false;
        try {
            // 尝试获取锁
            lock = Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(LOCK_RESET_USAGE, "locked"));
            if (lock) {
                log.info("get lock, start regularly reset usage");
                kookUserSettingService.resetTodayUsage();
            } else {
                // 获取锁失败，放弃任务执行
                log.warn("failed to get lock, abort reset usage");
            }
        } finally {
            // 释放锁
            if (lock) {
                stringRedisTemplate.delete(LOCK_CHECK_BILI_ROOM_KEY);
            }
        }
    }


}
