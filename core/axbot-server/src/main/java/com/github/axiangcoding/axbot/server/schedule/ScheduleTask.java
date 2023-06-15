package com.github.axiangcoding.axbot.server.schedule;

import com.github.axiangcoding.axbot.crawler.wt.WtCrawlerClient;
import com.github.axiangcoding.axbot.crawler.wt.entity.NewParseResult;
import com.github.axiangcoding.axbot.engine.NotificationEvent;
import com.github.axiangcoding.axbot.engine.SupportPlatform;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpNotificationInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookNotificationInput;
import com.github.axiangcoding.axbot.remote.bilibili.BiliClient;
import com.github.axiangcoding.axbot.remote.bilibili.service.entity.BiliResponse;
import com.github.axiangcoding.axbot.remote.bilibili.service.entity.resp.RoomInfoData;
import com.github.axiangcoding.axbot.remote.botmarket.BotMarketClient;
import com.github.axiangcoding.axbot.server.cache.CacheKeyGenerator;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import com.github.axiangcoding.axbot.server.data.entity.QGroupSetting;
import com.github.axiangcoding.axbot.server.data.entity.WtNews;
import com.github.axiangcoding.axbot.server.data.repository.WtNewsRepository;
import com.github.axiangcoding.axbot.server.service.*;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@EnableScheduling
public class ScheduleTask {

    @AllArgsConstructor
    @Getter
    public enum LOCK {
        CHECK_BILI_ROOM(CacheKeyGenerator.getCronJobLockKey("checkBiliRoom")),
        RESET_USAGE(CacheKeyGenerator.getCronJobLockKey("resetUsage")),
        LATEST_NEW(CacheKeyGenerator.getCronJobLockKey("latestWTNews")),
        SET_BOT_MARKET_ONLINE(CacheKeyGenerator.getCronJobLockKey("setBotMarketOnline")),
        ;
        private final String name;
    }

    @Resource
    StringRedisTemplate stringRedisTemplate;


    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    QUserSettingService qUserSettingService;

    @Resource
    WtCrawlerClient wtCrawlerClient;

    @Resource
    WtNewsRepository wtNewsRepository;

    @Resource
    KookGuildSettingService kookGuildSettingService;

    @Resource
    QGroupSettingService qGroupSettingService;

    @Resource
    BiliClient biliClient;

    @Resource
    BotService botService;

    @Resource
    BotMarketClient botMarketClient;

    @Resource
    BotConfProps botConfProps;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkBiliRoom() {
        RedisLockRunner redisLockRunner = new RedisLockRunner(stringRedisTemplate) {
            @Override
            public void run() {
                checkBiliRoomStatus();
            }
        };
        redisLockRunner.execute(LOCK.CHECK_BILI_ROOM);
    }

    @Scheduled(cron = "0 0/2 * * * ?")
    public void getWtLatestNews() {
        RedisLockRunner redisLockRunner = new RedisLockRunner(stringRedisTemplate) {
            @Override
            public void run() {
                checkWTLatestNews();
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
                qUserSettingService.resetTodayUsage();
            }
        };
        redisLockRunner.execute(LOCK.RESET_USAGE);
    }


    /**
     * 设置bot market 在线
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void botMarketOnline() {
        RedisLockRunner redisLockRunner = new RedisLockRunner(stringRedisTemplate) {
            @Override
            public void run() {
                String uuid = botConfProps.getBotMarket().getUuid();
                botMarketClient.setOnline(uuid);
            }
        };
        redisLockRunner.execute(LOCK.SET_BOT_MARKET_ONLINE);
    }

    public void resetLock(LOCK lock) {
        stringRedisTemplate.delete(lock.getName());
    }


    private void checkBiliRoomStatus() {
        if (botService.isPlatformEnabled(SupportPlatform.KOOK)) {
            List<KookGuildSetting> guilds = kookGuildSettingService.findByEnabledBiliLiveReminder();
            guilds.forEach(guild -> {
                String biliRoomId = guild.getFunctionSetting().getBiliRoomId();
                if (!StringUtils.isNumeric(biliRoomId)) {
                    return;
                }
                String biliLiveChannelId = guild.getFunctionSetting().getBiliLiveChannelId();
                BiliResponse<RoomInfoData> liveRoomInfo = biliClient.getLiveRoomInfo(biliRoomId);
                String cacheKey = CacheKeyGenerator.getKookBiliRoomRemindCacheKey(biliLiveChannelId, biliRoomId);
                RoomInfoData roomInfoData = liveRoomInfo.getData();
                if (roomInfoData.getLiveStatus() == 1) {
                    if (!Boolean.TRUE.equals(stringRedisTemplate.hasKey(cacheKey))) {
                        KookNotificationInput input = new KookNotificationInput();
                        input.setEvent(NotificationEvent.BILI_ROOM_REMIND);
                        input.setGuildId(guild.getGuildId());
                        input.setChannelId(biliLiveChannelId);
                        input.setData(roomInfoData);
                        botService.responseForNotificationAsync(SupportPlatform.KOOK, input);

                    }
                    stringRedisTemplate.opsForValue().set(cacheKey, "", 10, TimeUnit.MINUTES);
                }
            });
        }
        if (botService.isPlatformEnabled(SupportPlatform.CQHTTP)) {
            List<QGroupSetting> groups = qGroupSettingService.findByEnabledBiliLiveReminder();
            groups.forEach(group -> {
                String biliRoomId = group.getFunctionSetting().getBiliRoomId();
                if (!StringUtils.isNumeric(biliRoomId)) {
                    return;
                }
                BiliResponse<RoomInfoData> liveRoomInfo = biliClient.getLiveRoomInfo(biliRoomId);
                String cacheKey = CacheKeyGenerator.getCqhttpBiliRoomRemindCacheKey(group.getGroupId(), biliRoomId);
                RoomInfoData roomInfoData = liveRoomInfo.getData();
                if (roomInfoData.getLiveStatus() == 1) {
                    if (!Boolean.TRUE.equals(stringRedisTemplate.hasKey(cacheKey))) {
                        CqhttpNotificationInput input = new CqhttpNotificationInput();
                        input.setEvent(NotificationEvent.BILI_ROOM_REMIND);
                        input.setGroupId(group.getGroupId());
                        input.setData(roomInfoData);
                        botService.responseForNotificationAsync(SupportPlatform.CQHTTP, input);
                    }
                    stringRedisTemplate.opsForValue().set(cacheKey, "", 10, TimeUnit.MINUTES);
                }
            });
        }
    }

    private void checkWTLatestNews() {
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
            if (botService.isPlatformEnabled(SupportPlatform.KOOK)) {
                List<KookGuildSetting> guilds = kookGuildSettingService.findByEnableNewsReminder();
                guilds.forEach(guild -> {
                    String wtNewsChannelId = guild.getFunctionSetting().getWtNewsChannelId();
                    if (StringUtils.isEmpty(wtNewsChannelId)) {
                        return;
                    }
                    notExistNews.forEach(item -> {
                        KookNotificationInput input = new KookNotificationInput();
                        input.setEvent(NotificationEvent.WT_NEWS);
                        input.setGuildId(guild.getGuildId());
                        input.setChannelId(wtNewsChannelId);
                        input.setData(item);
                        botService.responseForNotificationAsync(SupportPlatform.KOOK, input);
                    });
                });
            }
            if (botService.isPlatformEnabled(SupportPlatform.CQHTTP)) {
                List<QGroupSetting> groups = qGroupSettingService.findByEnableNewsReminder();
                groups.forEach(group -> {
                    notExistNews.forEach(item -> {
                        CqhttpNotificationInput input = new CqhttpNotificationInput();
                        input.setEvent(NotificationEvent.WT_NEWS);
                        input.setGroupId(group.getGroupId());
                        input.setData(item);
                        botService.responseForNotificationAsync(SupportPlatform.CQHTTP, input);
                    });
                });
            }
        } catch (IOException e) {
            log.warn("get warthunder news failed", e);
        }
    }
}
