package com.github.axiangcoding.axbot.server.controller.v1;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.github.axiangcoding.axbot.bot.kook.service.entity.GuildListResp;
import com.github.axiangcoding.axbot.server.entity.CommonResult;
import com.github.axiangcoding.axbot.server.entity.vo.req.KookListGuild;
import com.github.axiangcoding.axbot.server.entity.vo.req.KookWebhookEvent;

import com.github.axiangcoding.axbot.server.service.BotKookService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1/bot/kook")
@Slf4j

public class BotKookController {
    @Resource
    BotKookService botKookService;

    @PostMapping("webhook")
    public Map<String, Object> webhook(@RequestBody String body) {
        KookWebhookEvent event = JSONObject.parseObject(body, KookWebhookEvent.class, JSONReader.Feature.SupportSmartMatch);
        log.info(JSONObject.toJSONString(event));
        String challenge = event.getD().getChallenge();
        HashMap<String, Object> map = new HashMap<>();
        map.put("challenge", challenge);
        return map;
    }

    @GetMapping("/guild/list")
    public CommonResult listGuild(@Valid @ParameterObject KookListGuild query) {
        List<GuildListResp.Item> items =
                botKookService.listGuild(query.getPage(),
                        query.getPageSize(),
                        query.getSort());
        return CommonResult.success("guilds", items);
    }
}
