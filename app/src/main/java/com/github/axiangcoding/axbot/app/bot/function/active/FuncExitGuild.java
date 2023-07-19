package com.github.axiangcoding.axbot.app.bot.function.active;

import com.github.axiangcoding.axbot.app.bot.annotation.AxActiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.ActiveEvent;
import com.github.axiangcoding.axbot.app.bot.function.AbstractActiveFunction;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.bot.Bot;

import java.util.Map;

@AxActiveFunc(event = ActiveEvent.EXIT_GUILD)
@Slf4j
public class FuncExitGuild extends AbstractActiveFunction {
    @Override
    public void processByKOOK(Bot bot, Map<String, Object> params) {
        log.warn("TBD FuncExitGuild.processByKOOK");
    }

    @Override
    public void processByQG(Bot bot, Map<String, Object> params) {
        log.warn("TBD FuncExitGuild.processByQG");
    }
}
