package com.github.axiangcoding.axbot.bot.kook.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * 构建Kook的卡片信息
 * 消息编辑器见 <a href="https://www.kookapp.cn/tools/message-builder.html#/card">消息编辑器</a>
 */
@Getter
@Setter
@ToString
public class KookCardMessage {
    String type;
    String theme;
    String size;
    String content;
    List<KookCardMessage> modules;
    List<KookCardMessage> elements;
    KookCardMessage text;
    String mode;
    Integer cols;
    List<KookCardMessage> fields;
    KookCardMessage accessory;
    Long startTime;
    Long endTime;

    public static List<KookCardMessage> defaultMsg(String title) {
        return defaultMsg(title, "info");
    }

    public static List<KookCardMessage> defaultMsg(String title, String theme) {
        List<KookCardMessage> messages = new ArrayList<>();
        KookCardMessage card = KookCardMessage.newCard(theme, "lg");
        ArrayList<KookCardMessage> modules = new ArrayList<>();

        modules.add(KookCardMessage.newHeader(title));
        modules.add(KookCardMessage.newDivider());

        card.setModules(modules);
        messages.add(card);
        return messages;
    }

    public static KookCardMessage newCard(String theme, String size) {
        KookCardMessage cardMessage = new KookCardMessage();
        cardMessage.setType("card");
        cardMessage.setTheme(theme);
        cardMessage.setSize(size);
        return cardMessage;
    }

    public static KookCardMessage newDivider() {
        KookCardMessage cardMessage = new KookCardMessage();
        cardMessage.setType("divider");
        return cardMessage;
    }

    public static KookCardMessage newHeader(String title) {
        KookCardMessage cardMessage = new KookCardMessage();
        cardMessage.setType("header");
        cardMessage.setText(newPlainText(title));
        return cardMessage;
    }

    public static KookCardMessage newContext(List<KookCardMessage> elements) {
        KookCardMessage cardMessage = new KookCardMessage();
        cardMessage.setType("context");
        cardMessage.setElements(elements);
        return cardMessage;
    }

    public static KookCardMessage newSection(KookCardMessage text) {
        KookCardMessage cardMessage = new KookCardMessage();
        cardMessage.setType("section");
        cardMessage.setText(text);
        return cardMessage;
    }

    public static KookCardMessage newPlainText(String content) {
        KookCardMessage cardMessage = new KookCardMessage();
        cardMessage.setType("plain-text");
        cardMessage.setContent(content);
        return cardMessage;
    }

    public static KookCardMessage newKMarkdown(String content) {
        KookCardMessage cardMessage = new KookCardMessage();
        cardMessage.setType("kmarkdown");
        cardMessage.setContent(content);
        return cardMessage;
    }

    public static KookCardMessage newButton(String theme, String text) {
        KookCardMessage cardMessage = new KookCardMessage();
        cardMessage.setType("button");
        cardMessage.setTheme(theme);
        cardMessage.setText(newPlainText(text));
        return cardMessage;
    }

    public static KookCardMessage newSectionWithLink(KookCardMessage text, KookCardMessage button) {
        KookCardMessage cardMessage = newSection(text);
        cardMessage.setMode("right");
        cardMessage.setAccessory(button);
        return cardMessage;
    }

    public static KookCardMessage newParagraph(int cols, List<KookCardMessage> fields) {
        KookCardMessage cardMessage = new KookCardMessage();
        cardMessage.setType("paragraph");
        cardMessage.setCols(cols);
        cardMessage.setFields(fields);
        return cardMessage;
    }

    public static KookCardMessage newCountDown(String mode, LocalDateTime startTime, LocalDateTime endTime) {
        KookCardMessage cardMessage = new KookCardMessage();
        cardMessage.setType("countdown");
        cardMessage.setMode(mode);
        cardMessage.setStartTime(startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        cardMessage.setEndTime(endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return cardMessage;
    }
}
