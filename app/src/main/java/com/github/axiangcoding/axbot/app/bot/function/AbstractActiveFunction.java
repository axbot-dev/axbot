package com.github.axiangcoding.axbot.app.bot.function;

import love.forte.simbot.bot.Bot;
import love.forte.simbot.component.kook.message.KookCardMessage;
import love.forte.simbot.component.qguild.message.QGContentText;
import love.forte.simbot.kook.objects.CardMessage;

import java.util.Map;

public abstract class AbstractActiveFunction {
    public abstract void processByKOOK(Bot bot, Map<String, Object> params);

    public abstract void processByQG(Bot bot, Map<String, Object> params);

    protected KookCardMessage toCardMessage(String text) {
        return KookCardMessage.asMessage(CardMessage.decode(text));
    }

    protected QGContentText toTextMessage(String text) {
        return new QGContentText(text);
    }
}
