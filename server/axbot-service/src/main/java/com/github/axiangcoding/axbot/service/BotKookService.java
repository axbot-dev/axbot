package com.github.axiangcoding.axbot.service;

import com.github.axiangcoding.axbot.kook.KookClient;
import com.github.axiangcoding.axbot.kook.service.entity.GuildListResp;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BotKookService {
    @Resource
    KookClient kookClient;

    public List<GuildListResp.Item> listGuild(Integer page, Integer pageSize, String sort) {
        List<GuildListResp.Item> list = new ArrayList<>();
        try {
            GuildListResp body = kookClient.guildService().getGuildList(page, pageSize, sort).execute().body();
            assert body != null;
            list.addAll(body.getData().getItems());
        } catch (Exception e) {
            log.warn("get guild list error", e);
        }
        return list;
    }
}
