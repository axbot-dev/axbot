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
        LATEST_NEW(CacheKeyGenerator.getCronJobLockKey("latestWTNews"));
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

    public void resetLock(LOCK lock) {
        stringRedisTemplate.delete(lock.getName());
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkBiliRoom() {
        if (!axBotService.isPlatformEnabled(AxBotSupportPlatform.PLATFORM_KOOK)) {
            return;
        }
        RedisLockRunner redisLockRunner = new RedisLockRunner(stringRedisTemplate) {
            @Override
            public void run() {
                botKookService.checkBiliRoomStatus();
            }
        };
        redisLockRunner.execute(LOCK.CHECK_BILI_ROOM);
    }

    @Scheduled(cron = "0 0/2 * * * ?")
    public void getWtLatestNews() {
        RedisLockRunner redisLockRunner = new RedisLockRunner(stringRedisTemplate) {
            @Override
            public void run() {
                try {
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
                } catch (IOException e) {
                    log.warn("get warthunder news failed", e);
                }

            }
        };
        redisLockRunner.execute(LOCK.LATEST_NEW);
    }

    @Scheduled(cron = "@daily")
    public void cleanUsage() {
        RedisLockRunner redisLockRunner = new RedisLockRunner(stringRedisTemplate) {
            @Override
            public void run() {
                kookUserSettingService.resetTodayUsage();
            }
        };
        redisLockRunner.execute(LOCK.RESET_USAGE);
    }


}
