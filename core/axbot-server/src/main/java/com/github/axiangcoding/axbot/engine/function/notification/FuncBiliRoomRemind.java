package com.github.axiangcoding.axbot.engine.function.notification;

import com.github.axiangcoding.axbot.engine.NotificationEvent;
import com.github.axiangcoding.axbot.engine.annot.AxbotNotificationFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractNotificationFunction;
import com.github.axiangcoding.axbot.engine.io.NotificationInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpNotificationInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpNotificationOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookNotificationInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookNotificationOutput;
import com.github.axiangcoding.axbot.remote.bilibili.service.entity.resp.RoomInfoData;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.engine.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.engine.template.KookQuickCard;
import org.springframework.stereotype.Component;

import java.util.List;

@AxbotNotificationFunc(event = NotificationEvent.BILI_ROOM_REMIND)
@Component
public class FuncBiliRoomRemind extends AbstractNotificationFunction {
    @Override
    public KookNotificationOutput execute(KookNotificationInput input) {
        RoomInfoData data = getRoomInfoData(input);
        String title = data.getTitle();
        String description = data.getDescription();
        Long roomId = data.getRoomId();

        KookQuickCard card = new KookQuickCard("来自AXBot的B站直播提醒", "success");

        card.addModule(KookCardMessage.newHeader("直播间: %s".formatted(title)));
        card.addModule(KookCardMessage.newContext(List.of(KookCardMessage.newKMarkdown(description))));
        card.addModule(KookCardMessage.newSection(KookCardMessage.newKMarkdown("大家快来一起观看吧 " + KookMDMessage.mention("here"))));
        card.addModule(KookCardMessage.quickTextLinkSection("点击按钮进入直播间观看", "跳转B站", "primary", "https://live.bilibili.com/%d".formatted(roomId)));
        return input.response(card.displayWithFooter());
    }

    @Override
    public CqhttpNotificationOutput execute(CqhttpNotificationInput input) {
        RoomInfoData data = getRoomInfoData(input);
        String title = data.getTitle();
        String description = data.getDescription();
        Long roomId = data.getRoomId();

        CqhttpQuickMsg msg = new CqhttpQuickMsg("来自AXBot的B站直播提醒");

        msg.addLine("直播间: %s".formatted(title));
        msg.addLine(description);
        msg.addLine("直播间链接: https://live.bilibili.com/%d".formatted(roomId));
        msg.addLine("大家快来一起观看吧");
        return input.response(msg.displayWithFooter());
    }

    private RoomInfoData getRoomInfoData(NotificationInput input) {
        return (RoomInfoData) input.getData();
    }
}
