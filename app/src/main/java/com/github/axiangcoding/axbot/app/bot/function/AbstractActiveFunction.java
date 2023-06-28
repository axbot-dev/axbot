package com.github.axiangcoding.axbot.app.bot.function;

import love.forte.simbot.bot.Bot;

import java.util.Map;

public abstract class AbstractActiveFunction {
    public abstract void processByKOOK(Bot bot, Map<String, Object> params);

    public abstract void processByQG(Bot bot, Map<String, Object> params);
}
