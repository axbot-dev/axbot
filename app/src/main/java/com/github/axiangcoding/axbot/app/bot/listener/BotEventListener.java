package com.github.axiangcoding.axbot.app.bot.listener;

import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.function.FunctionHandler;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.ChannelMessageEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BotEventListener {
    @Resource
    FunctionHandler handler;

    /**
     * 监听频道消息
     *
     * @param event
     */
    @Listener
    @Filter(targets = @Filter.Targets(atBot = true))
    @WithSpan
    public void onEvent(ChannelMessageEvent event) {
        String eventId = event.getId().toString();
        String guildId = event.getChannel().getGuildId().toString();
        String channelId = event.getChannel().getId().toString();
        String messageId = event.getMessageContent().getMessageId().toString();
        String authorId = event.getAuthor().getId().toString();
        String botId = event.getBot().getId().toString();
        String botComponentId = event.getBot().getComponent().getId();
        String plainText = event.getMessageContent().getPlainText();
        log.info("收到频道消息: eventId={}, guildId={}, channelId={}, messageId={}, authorId={}, botId={}, botComponentId={}, plainText={}",
                eventId, guildId, channelId, messageId, authorId, botId, botComponentId, plainText);
        if ("simbot.kook".equals(botComponentId)) {
            handler.processPassiveFunction(event, BotPlatform.KOOK);
        } else if ("simbot.qqguild".equals(botComponentId)){
            handler.processPassiveFunction(event, BotPlatform.QQ_GUILD);
        }
    }

    // FIXME 暂时无法处理卡片按钮回调事件
    // @Listener
    // @WithSpan
    // public void onKookEvent(MessageBtnClickEvent event) {
    //     System.out.println(event.toString());
    // }

}
