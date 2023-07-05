package com.github.axiangcoding.axbot.app.bot.function;

import love.forte.simbot.component.kook.message.KookCardMessage;
import love.forte.simbot.component.qguild.message.QGContentText;
import love.forte.simbot.event.ChannelMessageEvent;
import love.forte.simbot.kook.objects.CardMessage;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractPassiveFunction {

    public abstract void processForKOOK(ChannelMessageEvent event);

    public abstract void processForQG(ChannelMessageEvent event);

    protected KookCardMessage toCardMessage(String text) {
        return KookCardMessage.asMessage(CardMessage.decode(text));
    }

    protected QGContentText toTextMessage(String text) {
        return new QGContentText(text);
    }

    protected String getInput(ChannelMessageEvent event) {
        return StringUtils.trim(event.getMessageContent().getPlainText());
    }

    protected String getGuildId(ChannelMessageEvent event) {
        return event.getSource().getGuild().getId().toString();
    }

    protected String getUserId(ChannelMessageEvent event) {
        return event.getAuthor().getId().toString();
    }
}
