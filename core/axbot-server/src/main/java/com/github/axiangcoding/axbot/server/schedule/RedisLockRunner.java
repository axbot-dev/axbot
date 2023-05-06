package com.github.axiangcoding.axbot.server.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

@Slf4j
public abstract class RedisLockRunner {
    private final StringRedisTemplate stringRedisTemplate;

    public RedisLockRunner(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public abstract void run();

    public void execute(ScheduleTask.LOCK taskLock) {
        boolean lock = false;
        String lockName = taskLock.getName();
        try {
            // 尝试获取锁
            lock = Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(lockName, "locked"));
            if (lock) {
                log.info("get lock, start schedule task {}", lockName);
                run();

            } else {
                // 获取锁失败，放弃任务执行
                log.warn("failed to get lock, abort schedule task {}", lockName);
            }
        } catch (Exception e) {
            log.warn("schedule task {} run failed", lockName, e);
        } finally {
            // 释放锁
            if (lock) {
                stringRedisTemplate.delete(lockName);
            }
        }
    }

}
