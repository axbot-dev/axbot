package com.github.axiangcoding.axbot.service;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.crawler.WtCrawlerClient;
import com.github.axiangcoding.axbot.crawler.entity.ParserResult;
import com.github.axiangcoding.axbot.entity.CrawlerMissionMessage;
import com.github.axiangcoding.axbot.entity.GlobalSetting;
import com.github.axiangcoding.axbot.entity.Mission;
import com.github.axiangcoding.axbot.entity.WtGamerProfile;
import com.github.axiangcoding.axbot.repository.GlobalSettingRepository;
import com.github.axiangcoding.axbot.repository.WtGamerProfileRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class WTGameProfileService {

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
    Queue outQueue;

    public Optional<WtGamerProfile> findByNickname(String nickname) {
        return wtGamerProfileRepository.findByNickname(nickname);
    }

    public void upsertByNickname(String nickname, WtGamerProfile gp) {
        Optional<WtGamerProfile> optGp = findByNickname(nickname);
        optGp.ifPresent(item -> {
            gp.setId(item.getId());
            gp.setCreateTime(LocalDateTime.now());
        });

        gp.setUpdateTime(LocalDateTime.now());
        wtGamerProfileRepository.save(gp);
    }

    /**
     * 更新游戏数据
     *
     * @param nickname
     * @return
     */
    public Mission submitMissionUpdateProfile(String nickname) {
        Mission mission = new Mission();
        final String missionId = mission.getMissionId().toString();
        missionService.save(mission);
        // 提交线程去执行任务
        threadPoolTaskExecutor.execute(() -> {
            missionService.setPending(missionId);
            executeMission(missionId, nickname);
        });
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
        Optional<GlobalSetting> optKey = globalSettingRepository.findByKey(GlobalSetting.KEY_WT_PROFILE_CRAWLER_MODE);
        // 如果配置项为空或者为direct模式，直接获取数据即可
        if (optKey.isEmpty() || optKey.get().getValue().equals(WtCrawlerClient.MODE_DIRECT)) {
            try {
                missionService.setRunning(missionId, 20.0);
                ParserResult o = wtCrawlerClient.getProfileFromUrl(nickname);
                // FIXME 保存数据到游戏数据表中
                log.info("todo o is {}", o);
            } catch (IOException e) {
                log.info("get wt profile failed", e);
                missionService.setFailed(missionId, e);
            }
        }
        // 如果是selenium模式，则提交任务到队列中
        else if (optKey.get().getValue().equals(WtCrawlerClient.MODE_SELENIUM)) {
            CrawlerMissionMessage msg = new CrawlerMissionMessage();
            msg.setMissionId(missionId);
            msg.setUrl(WtCrawlerClient.formatGetProfileUrl(nickname));
            msg.setXpathCondition(WtCrawlerClient.EXIST_XPATH_CONDITION);
            String outQueueName = outQueue.getName();
            rabbitTemplate.convertAndSend(outQueueName, JSONObject.toJSONString(msg));
            missionService.setRunning(missionId, 50.0);
            log.info("send a message to {}, missionId is {}", outQueueName, missionId);
        }
    }


}
