package com.github.axiangcoding.axbot.server.service.axbot.function.notification;

import com.github.axiangcoding.axbot.crawler.wt.entity.NewParseResult;
import com.github.axiangcoding.axbot.engine.v1.function.NotificationFunction;
import com.github.axiangcoding.axbot.engine.v1.io.NotificationInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpNotificationInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpNotificationOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookNotificationInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookNotificationOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FuncWTNews extends NotificationFunction {
    @Override
    public KookNotificationOutput execute(KookNotificationInput input) {
        NewParseResult parseResult = getNewParseResult(input);
        String title = parseResult.getTitle();
        String comment = parseResult.getComment();
        String dateStr = parseResult.getDateStr();
        String url = parseResult.getUrl();

        KookQuickCard card = new KookQuickCard("AXBot带来战雷的最新新闻", "success");

        card.addModule(KookCardMessage.newHeader("%s".formatted(title)));
        card.addModule(KookCardMessage.newContext(List.of(KookCardMessage.newKMarkdown(dateStr))));
        card.addModule(KookCardMessage.quickMdSection(comment));
        // modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown(KookMDMessage.mention("here"))));
        card.addModule(KookCardMessage.newDivider());
        card.addModule(KookCardMessage.quickTextLinkSection("点击按钮跳转到官网", "查看原文", "info", url));
        return input.response(card.displayWithFooter());
    }

    @Override
    public CqhttpNotificationOutput execute(CqhttpNotificationInput input) {
        NewParseResult parseResult = getNewParseResult(input);
        String title = parseResult.getTitle();
        String comment = parseResult.getComment();
        String dateStr = parseResult.getDateStr();

        CqhttpQuickMsg quickMsg = new CqhttpQuickMsg("AXBot带来战雷的最新新闻");
        quickMsg.addLine("%s".formatted(title));
        quickMsg.addLine(dateStr);
        quickMsg.addLine(comment);
        return input.response(quickMsg.displayWithFooter());
    }

    private NewParseResult getNewParseResult(NotificationInput input) {
        return ((NewParseResult) input.getData());
    }
}
