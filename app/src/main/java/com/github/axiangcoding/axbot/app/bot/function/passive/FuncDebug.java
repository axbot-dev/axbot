package com.github.axiangcoding.axbot.app.bot.function.passive;

import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.FunctionType;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.server.service.MessageQueueService;
import com.github.axiangcoding.axbot.app.third.pubg.PubgClient;
import jakarta.annotation.Resource;
import love.forte.simbot.event.ChannelMessageEvent;

@AxPassiveFunc(command = FunctionType.DEBUG)
public class FuncDebug extends AbstractPassiveFunction {
    @Resource
    private MessageQueueService messageQueueService;

    @Resource
    PubgClient pubgClient;

    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        // messageQueueService.sendCrawlerMessage("test","https://warthunder.com/zh/community/userinfo/?nick=OnTheRocks", WtCrawlerClient.EXIST_PROFILE_XPATH_CONDITION);
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {

    }
}
