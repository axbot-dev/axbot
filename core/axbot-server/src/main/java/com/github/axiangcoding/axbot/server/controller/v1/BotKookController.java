package com.github.axiangcoding.axbot.server.controller.v1;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.KookWebhookEvent;
import com.github.axiangcoding.axbot.server.service.BotKookService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("v1/bot/kook")
@Slf4j

public class BotKookController {
    @Resource
    BotKookService botKookService;

    @PostMapping("webhook")
    public Map<String, Object> webhook(@RequestBody String body) {
        KookWebhookEvent event = JSONObject.parseObject(body, KookWebhookEvent.class,
                JSONReader.Feature.SupportSmartMatch);
        if (!botKookService.compareVerifyToken(event.getD().getVerifyToken())) {
            log.warn("no a valid kook webhook message");
            return new HashMap<>();
        }
        log.info(JSONObject.toJSONString(event));
        return botKookService.DetermineMessageResponse(event);
    }

}
