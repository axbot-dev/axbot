package com.github.axiangcoding.axbot.app.server.controller.v2;

import com.github.axiangcoding.axbot.app.server.configuration.annotation.RequireApiKey;
import com.github.axiangcoding.axbot.app.server.controller.entity.CommonResult;
import com.github.axiangcoding.axbot.app.server.controller.v2.entity.resp.BotInfoResp;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.application.Application;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("manage/bot")
@Slf4j
public class ManageBotController {
    @Resource
    Application application;

    @RequireApiKey(superAdmin = true)
    @GetMapping()
    public CommonResult getBots() {
        List<BotInfoResp> infos = new ArrayList<>();
        for (BotManager<?> botManager : application.getBotManagers()) {
            for (Bot bot : botManager.all()) {
                infos.add(new BotInfoResp()
                        .setUsername(bot.getUsername())
                        .setId(bot.getId().toString())
                        .setComponent(bot.getComponent().getId())
                        .setIsActive(bot.isActive())
                );
            }
        }
        return CommonResult.success("bots", infos);
    }
}
