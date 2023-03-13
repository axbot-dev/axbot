package com.github.axiangcoding.axbot.controller.v1;

import com.github.axiangcoding.axbot.entity.vo.KookWebhookEvent;
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

    @PostMapping("webhook")
    public Map<String, Object> webhook(@RequestBody KookWebhookEvent event) {
        String challenge = event.getD().getChallenge();
        HashMap<String, Object> map = new HashMap<>();
        map.put("challenge", challenge);
        return map;
    }
}
