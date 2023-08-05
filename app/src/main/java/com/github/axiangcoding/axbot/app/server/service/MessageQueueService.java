package com.github.axiangcoding.axbot.app.server.service;


import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.app.crawler.WtCrawlerClient;
import com.github.axiangcoding.axbot.app.server.configuration.RabbitMqConfiguration;
import com.github.axiangcoding.axbot.app.server.service.entity.CrawlerMissionMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageQueueService {
    @Resource
    Queue inQueue;

    @Resource
    Queue outQueue;

    @Resource
    RabbitTemplate rabbitTemplate;

    // @Resource
    // WTGamerProfileService wtGamerProfileService;
    //
    // @Resource
    // MissionService missionService;

    @Resource
    WtCrawlerClient wtCrawlerClient;

    public void sendCrawlerMessage(String missionId, String url, String xpathCondition) {
        CrawlerMissionMessage message = new CrawlerMissionMessage();
        message.setMissionId(missionId);
        message.setUrl(url);
        message.setXpathCondition(xpathCondition);
        sendCrawlerMessage(message);
    }

    public void sendCrawlerMessage(CrawlerMissionMessage message) {
        rabbitTemplate.convertAndSend(RabbitMqConfiguration.OUT_QUEUE_NAME, JSONObject.toJSONString(message));
    }


    @RabbitListener(queues = RabbitMqConfiguration.IN_QUEUE_NAME)
    public void receiveCrawlerResultMessage(String message) {
        log.info("receive message back, message is {}", message);
        // try {
        //     CrawlerResultMessage msg = JsonUtils.fromJson(message, CrawlerResultMessage.class);
        //     String missionId = msg.getMissionId();
        //     log.info("receive message back, missionId is {}", missionId);
        //     Optional<Mission> optMission = missionService.findByMissionId(missionId);
        //     if (optMission.isEmpty()) {
        //         log.warn("no such mission!");
        //         return;
        //     }
        //     ProfileParseResult pr = wtCrawlerClient.getProfileFromHtml(msg.getPageSource());
        //     // 找到用户资料
        //     if (pr.getFound()) {
        //         WtGamerProfile wtGamerProfile = WtGamerProfile.from(pr.getProfile());
        //         wtGamerProfileService.upsertByNickname(wtGamerProfile.getNickname(), wtGamerProfile);
        //     } else {
        //         log.info("user not found at mission {}", missionId);
        //
        //     }
        //     missionService.setSuccess(missionId, JsonUtils.toJson(pr));
        // } catch (Exception e) {
        //     log.warn(e.getMessage(), e);
        // }
    }
}
