package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.crawler.wt.WtCrawlerClient;
import com.github.axiangcoding.axbot.crawler.wt.entity.ProfileParseResult;
import com.github.axiangcoding.axbot.server.cache.CacheKeyGenerator;
import com.github.axiangcoding.axbot.server.data.entity.GlobalSetting;
import com.github.axiangcoding.axbot.server.data.entity.Mission;
import com.github.axiangcoding.axbot.server.data.entity.WtGamerProfile;
import com.github.axiangcoding.axbot.server.data.repository.GlobalSettingRepository;
import com.github.axiangcoding.axbot.server.data.repository.WtGamerProfileRepository;
import com.github.axiangcoding.axbot.server.service.entity.CrawlerMissionMessage;
import com.github.axiangcoding.axbot.server.util.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WTGamerProfileService {

    @Resource
    WtGamerProfileRepository wtGamerProfileRepository;

    @Resource
    MissionService missionService;

    @Resource
    WtCrawlerClient wtCrawlerClient;

    @Resource
    GlobalSettingRepository globalSettingRepository;

    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    Queue outQueue;

    public Optional<WtGamerProfile> findByNickname(String nickname) {
        return wtGamerProfileRepository.findByNickname(nickname);
    }

    public void upsertByNickname(String nickname, WtGamerProfile gp) {
        Optional<WtGamerProfile> optGp = findByNickname(nickname);
        optGp.ifPresent(item -> {
            gp.setId(item.getId());
            gp.setCreateTime(item.getCreateTime());
        });
        gp.setUpdateTime(LocalDateTime.now());
        wtGamerProfileRepository.save(gp);
    }

    public boolean canBeRefresh(String nickname) {
        Boolean hasKey = stringRedisTemplate.hasKey(CacheKeyGenerator.getWtGamerProfileUpdateCacheKey(nickname));
        return Boolean.FALSE.equals(hasKey);
    }

    public void putRefreshFlag(String nickname) {
        stringRedisTemplate.opsForValue().set(CacheKeyGenerator.getWtGamerProfileUpdateCacheKey(nickname), "", 1, TimeUnit.DAYS);
    }

    public void deleteRefreshFlag(String nickname) {
        stringRedisTemplate.opsForValue().getAndDelete(CacheKeyGenerator.getWtGamerProfileUpdateCacheKey(nickname));
    }

    /**
     * 更新游戏数据
     *
     * @param nickname
     * @return
     */
    public Mission submitMissionToUpdate(String nickname) {
        Mission mission = new Mission();
        final String missionId = mission.getMissionId().toString();
        missionService.save(mission);
        // 提交线程去执行任务
        threadPoolTaskExecutor.execute(() -> {
            try {
                missionService.setPending(missionId);
                executeMission(missionId, nickname);
            } catch (Exception e) {
                log.error("execute mission error", e);
            }
        });
        putRefreshFlag(nickname);
        return mission;
    }

    /**
     * 执行获取游戏数据的任务
     *
     * @param missionId
     * @param nickname
     */
    private void executeMission(String missionId, String nickname) {
        log.info("start mission {} to update WT profile", missionId);
        Optional<Mission> optMission = missionService.findByMissionId(missionId);
        if (optMission.isEmpty()) {
            log.warn("no such mission exist!");
            return;
        }
        missionService.setRunning(missionId, 10.0);
        Optional<GlobalSetting> optKey = globalSettingRepository.findByKey(GlobalSetting.KEY.WT_PROFILE_CRAWLER_MODE.getLabel());
        // 如果配置项为空或者为direct模式，直接获取数据即可
        if (optKey.isEmpty() || optKey.get().getValue().equals(WtCrawlerClient.MODE.DIRECT.getName())) {
            try {
                missionService.setRunning(missionId, 20.0);
                ProfileParseResult pr = wtCrawlerClient.getProfileFromUrl(nickname);
                missionService.setRunning(missionId, 50.0);
                // 找到用户资料
                if (pr.getFound()) {
                    WtGamerProfile wtGamerProfile = WtGamerProfile.from(pr.getProfile());
                    upsertByNickname(wtGamerProfile.getNickname(), wtGamerProfile);
                } else {
                    log.info("user not found at mission {}", missionId);
                }
                missionService.setSuccess(missionId, JsonUtils.toJson(pr));
            } catch (IOException e) {
                log.info("get wt profile failed", e);
                missionService.setFailed(missionId, e);
            }
        }
        // 如果是selenium模式，则提交任务到队列中
        else if (optKey.get().getValue().equals(WtCrawlerClient.MODE.SELENIUM.getName())) {
            CrawlerMissionMessage msg = new CrawlerMissionMessage();
            msg.setMissionId(missionId);
            msg.setUrl(WtCrawlerClient.formatGetProfileUrl(nickname));
            msg.setXpathCondition(WtCrawlerClient.EXIST_PROFILE_XPATH_CONDITION);
            String outQueueName = outQueue.getName();
            rabbitTemplate.convertAndSend(outQueueName, JsonUtils.toJson(msg));
            missionService.setRunning(missionId, 50.0);
            log.info("send a message to {}, missionId is {}", outQueueName, missionId);
        }
    }

}
