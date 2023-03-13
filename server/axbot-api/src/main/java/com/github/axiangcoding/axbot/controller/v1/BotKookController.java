package com.github.axiangcoding.axbot.controller.v1;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
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
    public Map<String, Object> webhook(@RequestBody String body) {
        KookWebhookEvent event = JSONObject.parseObject(body, KookWebhookEvent.class, JSONReader.Feature.SupportSmartMatch);
        log.info(JSONObject.toJSONString(event));
        String challenge = event.getD().getChallenge();
        HashMap<String, Object> map = new HashMap<>();
        map.put("challenge", challenge);
        return map;
    }
}
