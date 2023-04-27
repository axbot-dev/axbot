package com.github.axiangcoding.axbot.server.schedule;

import com.github.axiangcoding.axbot.crawler.wt.WtCrawlerClient;
import com.github.axiangcoding.axbot.crawler.wt.entity.NewParseResult;
import com.github.axiangcoding.axbot.engine.entity.AxBotSupportPlatform;
import com.github.axiangcoding.axbot.server.cache.CacheKeyGenerator;
import com.github.axiangcoding.axbot.server.data.entity.WtNews;
import com.github.axiangcoding.axbot.server.data.repository.WtNewsRepository;
import com.github.axiangcoding.axbot.server.service.AxBotService;
import com.github.axiangcoding.axbot.server.service.BotKookService;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@EnableScheduling
public class ScheduleTask {

    @AllArgsConstructor
    @Getter
    public enum LOCK {
        CHECK_BILI_ROOM(CacheKeyGenerator.getCronJobLockKey("checkBiliRoom")),
        RESET_USAGE(CacheKeyGenerator.getCronJobLockKey("resetUsage")),
        LATEST_NEW(CacheKeyGenerator.getCronJobLockKey("latestWTNews"))
        ;
        private final String name;
    }

    @Resource
    BotKookService botKookService;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    AxBotService axBotService;

    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    WtCrawlerClient wtCrawlerClient;

    @Resource
    WtNewsRepository wtNewsRepository;

    public void resetLock(LOCK lock){
        stringRedisTemplate.delete(lock.getName());
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkBiliRoom() {
        if (!axBotService.isPlatformEnabled(AxBotSupportPlatform.PLATFORM_KOOK)) {
            return;
        }

        boolean lock = false;
        try {
            // 尝试获取锁
            lock = Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(LOCK.CHECK_BILI_ROOM.getName(), "locked"));
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
                stringRedisTemplate.delete(LOCK.CHECK_BILI_ROOM.getName());
            }
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void getWtLatestNews() {
        boolean lock = false;
        try {
            // 尝试获取锁
            lock = Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(LOCK.LATEST_NEW.getName(), "locked"));
            if (lock) {
                log.info("get lock, start regularly checking the news of warthunder");
                List<NewParseResult> zhNews = wtCrawlerClient.getNewsFromUrl(WtCrawlerClient.REGION.ZH);
                List<NewParseResult> enNews = wtCrawlerClient.getNewsFromUrl(WtCrawlerClient.REGION.EN);
                zhNews.addAll(enNews);

                List<NewParseResult> notExistNews = zhNews.stream().filter((item) -> {
                    String url = item.getUrl();
                    boolean exists = wtNewsRepository.existsByUrl(url);
                    if (!exists) {
                        WtNews entity = new WtNews();
                        entity.setUrl(item.getUrl());
                        entity.setTitle(item.getTitle());
                        entity.setPosterUrl(item.getPosterUrl());
                        entity.setComment(item.getComment());
                        entity.setDateStr(item.getDateStr());
                        wtNewsRepository.save(entity);
                    }
                    return !exists;
                }).toList();
                if (axBotService.isPlatformEnabled(AxBotSupportPlatform.PLATFORM_KOOK)) {
                    notExistNews.forEach(item -> botKookService.sendLatestNews(item));
                }
            } else {
                log.warn("failed to get lock, abort checking the news of warthunder");
            }
        } catch (IOException e) {
            log.warn("get warthunder latest news failed", e);
        } finally {
            // 释放锁
            if (lock) {
                stringRedisTemplate.delete(LOCK.LATEST_NEW.getName());
            }
        }
    }

    @Scheduled(cron = "@daily")
    public void cleanUsage() {
        boolean lock = false;
        try {
            // 尝试获取锁
            lock = Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(LOCK.RESET_USAGE.getName(), "locked"));
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
                stringRedisTemplate.delete(LOCK.RESET_USAGE.getName());
            }
        }
    }


}
