package com.github.axiangcoding.axbot.service;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.crawler.WtCrawlerClient;
import com.github.axiangcoding.axbot.crawler.entity.ParserResult;
import com.github.axiangcoding.axbot.entity.CrawlerResultMessage;
import com.github.axiangcoding.axbot.data.entity.Mission;
import com.github.axiangcoding.axbot.data.entity.WtGamerProfile;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class MessageQueueService {
    public static final String IN_QUEUE_NAME = "crawler_result";
    public static final String OUT_QUEUE_NAME = "crawler_mission";


    @Resource
    WTGameProfileService wtGameProfileService;

    @Resource
    MissionService missionService;

    @Resource
    WtCrawlerClient wtCrawlerClient;

    @RabbitListener(queues = IN_QUEUE_NAME)
    public void receiveMessage(String message) throws IOException {
        CrawlerResultMessage msg = JSONObject.parseObject(message, CrawlerResultMessage.class);
        String missionId = msg.getMissionId();
        try {
            log.info("receive message back, missionId is {}", missionId);
            Optional<Mission> optMission = missionService.findByMissionId(missionId);
            if (optMission.isEmpty()) {
                log.warn("no such mission!");
                return;
            }
            ParserResult pr = wtCrawlerClient.getProfileFromHtml(msg.getPageSource());
            // 找到用户资料
            if (pr.getFound()) {
                WtGamerProfile wtGamerProfile = WtGamerProfile.from(pr.getProfile());
                wtGameProfileService.upsertByNickname(wtGamerProfile.getNickname(), wtGamerProfile);
            } else {
                log.info("user not found at mission {}", missionId);

            }
            missionService.setSuccess(missionId, JSONObject.from(pr));
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            missionService.setFailed(missionId, e);
        }
    }
}
