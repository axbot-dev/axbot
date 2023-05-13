package com.github.axiangcoding.axbot.server.service.axbot.function.notification;

import com.github.axiangcoding.axbot.engine.v1.function.NotificationFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpNotificationInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpNotificationOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookNotificationInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookNotificationOutput;
import com.github.axiangcoding.axbot.remote.kook.KookClient;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookGuild;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookResponse;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.service.KookGuildSettingService;
import com.github.axiangcoding.axbot.server.service.QGroupSettingService;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class FuncJoinGuild extends NotificationFunction {
    @Resource
    KookGuildSettingService kookGuildSettingService;

    @Resource
    QGroupSettingService qGroupSettingService;

    @Resource
    BotConfProps botConfProps;

    @Resource
    KookClient kookClient;

    @Override
    public KookNotificationOutput execute(KookNotificationInput input) {
        String guildId = input.getGuildId();
        kookGuildSettingService.updateWhenJoin(guildId);
        KookResponse<KookGuild> guildView = kookClient.getGuildView(guildId);
        String defaultChannelId = guildView.getData().getDefaultChannelId();
        input.setChannelId(defaultChannelId);

        KookQuickCard quickCard = new KookQuickCard("大家好，我是AXBot", "success");
        String l1 = "现在是北京时间: "
                + KookMDMessage.italic(
                LocalDateTime.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        String prefix = botConfProps.getTriggerMessagePrefix().get(0);
        String l2 = "需要我为你提供什么服务呢？如果你不知道怎么开始，聊天框输入 "
                + KookMDMessage.code("%s 帮助".formatted(prefix)) + " 开始探索";

        quickCard.addModule(KookCardMessage.quickMdSection(l1));
        quickCard.addModule(KookCardMessage.quickMdSection("当你看到这条消息时，代表我刚加入这个服务器"));
        quickCard.addModule(KookCardMessage.quickMdSection(l2));

        return input.response(quickCard.displayWithFooter());


    }

    @Override
    public CqhttpNotificationOutput execute(CqhttpNotificationInput input) {
        qGroupSettingService.updateWhenJoin(input.getGroupId());
        CqhttpQuickMsg quickMsg = new CqhttpQuickMsg("大家好，我是AXBot");
        String l1 = "现在是北京时间: "
                + LocalDateTime.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        quickMsg.addLine(l1);
        quickMsg.addLine("当你看到这条消息时，代表我刚加入这个群");
        String prefix = botConfProps.getTriggerMessagePrefix().get(0);
        quickMsg.addLine("需要我为你提供什么服务呢？如果你不知道怎么开始，聊天框输入 `%s 帮助` 开始探索".formatted(prefix));
        return input.response(quickMsg.displayWithFooter());
    }
}
