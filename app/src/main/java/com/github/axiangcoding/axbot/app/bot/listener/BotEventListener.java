package com.github.axiangcoding.axbot.app.bot.listener;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.app.bot.enums.ActiveEvent;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.enums.ClickBtnEvent;
import com.github.axiangcoding.axbot.app.bot.function.FunctionHandler;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.component.kook.event.KookBotSelfJoinedGuildEvent;
import love.forte.simbot.component.kook.event.KookMessageBtnClickEvent;
import love.forte.simbot.event.ChannelMessageEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
        log.info("received channel message: eventId={}, guildId={}, channelId={}, messageId={}, authorId={}, botId={}, botComponentId={}, plainText={}",
                eventId, guildId, channelId, messageId, authorId, botId, botComponentId, plainText);
        if ("simbot.kook".equals(botComponentId)) {
            handler.processPassiveFunction(event, BotPlatform.KOOK);
        } else if ("simbot.qqguild".equals(botComponentId)) {
            handler.processPassiveFunction(event, BotPlatform.QQ_GUILD);
        }
    }

    @Listener
    @WithSpan
    public void onEvent(KookBotSelfJoinedGuildEvent event) {

    }

    /**
     * 监听kook的卡片按钮点击事件
     *
     * @param event
     */
    @Listener
    @WithSpan
    public void onKookEvent(KookMessageBtnClickEvent event) {
        String val = event.getValue();
        String userId = event.getUserId().toString();
        JSONObject json = JSONObject.parseObject(val);
        String type = json.getString("type");
        try {
            ClickBtnEvent clickBtnEvent = ClickBtnEvent.valueOf(type);
            if (clickBtnEvent == ClickBtnEvent.BUG_REPORT) {
                Map<String, Object> map = new HashMap<>();
                map.put("trace_id", json.getString("trace_id"));
                map.put("user_id", userId);
                handler.triggerEvent(ActiveEvent.REPORT_TRACE, BotPlatform.KOOK, map);
            } else if (clickBtnEvent == ClickBtnEvent.GET_ROLE) {
                Map<String, Object> map = new HashMap<>();
                map.put("role_id", json.getString("role_id"));
                map.put("user_id", userId);
                handler.triggerEvent(ActiveEvent.GET_ROLE, BotPlatform.KOOK, map);
            }
        } catch (IllegalArgumentException e) {
            log.error("not supported kook click btn event: {}", type);
        }
    }

}
