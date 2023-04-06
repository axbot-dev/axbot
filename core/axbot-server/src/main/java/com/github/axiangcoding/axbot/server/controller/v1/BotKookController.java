package com.github.axiangcoding.axbot.server.controller.v1;

import com.github.axiangcoding.axbot.server.controller.entity.vo.req.KookWebhookEvent;
import com.github.axiangcoding.axbot.server.service.BotKookService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
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
        log.info("receive kook webhook msg, plain: {}", body);
        GsonBuilder gb = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        KookWebhookEvent event = gb.create().fromJson(body, KookWebhookEvent.class);
        if (!botKookService.compareVerifyToken(event.getD().getVerifyToken())) {
            log.warn("no a valid kook webhook message");
            return new HashMap<>();
        }
        return botKookService.DetermineMessageResponse(event);
    }

}
