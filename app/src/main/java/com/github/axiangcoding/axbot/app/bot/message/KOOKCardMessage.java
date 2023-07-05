package com.github.axiangcoding.axbot.app.bot.message;

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
public class KOOKCardMessage {
    String type;
    String theme;
    String size;
    String content;
    List<KOOKCardMessage> modules;
    List<KOOKCardMessage> elements;
    KOOKCardMessage text;
    String mode;
    Integer cols;
    List<KOOKCardMessage> fields;
    KOOKCardMessage accessory;
    Long startTime;
    Long endTime;
    String value;
    String click;

    public static List<KOOKCardMessage> defaultMsg(String title) {
        return defaultMsg(title, "info");
    }

    public static List<KOOKCardMessage> defaultMsg(String title, String theme) {
        List<KOOKCardMessage> messages = new ArrayList<>();
        KOOKCardMessage card = KOOKCardMessage.newCard(theme, "lg");
        ArrayList<KOOKCardMessage> modules = new ArrayList<>();

        modules.add(KOOKCardMessage.newHeader(title));
        modules.add(KOOKCardMessage.newDivider());

        card.setModules(modules);
        messages.add(card);
        return messages;
    }

    public static KOOKCardMessage newCard(String theme, String size) {
        KOOKCardMessage cardMessage = new KOOKCardMessage();
        cardMessage.setType("card");
        cardMessage.setTheme(theme);
        cardMessage.setSize(size);
        return cardMessage;
    }

    public static KOOKCardMessage newDivider() {
        KOOKCardMessage cardMessage = new KOOKCardMessage();
        cardMessage.setType("divider");
        return cardMessage;
    }

    public static KOOKCardMessage newHeader(String title) {
        KOOKCardMessage cardMessage = new KOOKCardMessage();
        cardMessage.setType("header");
        cardMessage.setText(newPlainText(title));
        return cardMessage;
    }

    public static KOOKCardMessage newContext(List<KOOKCardMessage> elements) {
        KOOKCardMessage cardMessage = new KOOKCardMessage();
        cardMessage.setType("context");
        cardMessage.setElements(elements);
        return cardMessage;
    }

    public static KOOKCardMessage newSection(KOOKCardMessage text) {
        KOOKCardMessage cardMessage = new KOOKCardMessage();
        cardMessage.setType("section");
        cardMessage.setText(text);
        return cardMessage;
    }


    public static KOOKCardMessage newPlainText(String content) {
        KOOKCardMessage cardMessage = new KOOKCardMessage();
        cardMessage.setType("plain-text");
        cardMessage.setContent(content);
        return cardMessage;
    }

    public static KOOKCardMessage newKMarkdown(String content) {
        KOOKCardMessage cardMessage = new KOOKCardMessage();
        cardMessage.setType("kmarkdown");
        cardMessage.setContent(content);
        return cardMessage;
    }

    public static KOOKCardMessage newButton(String theme, String text, String click, String value) {
        KOOKCardMessage cardMessage = new KOOKCardMessage();
        cardMessage.setType("button");
        cardMessage.setTheme(theme);
        cardMessage.setText(newKMarkdown(text));
        cardMessage.setClick(click);
        cardMessage.setValue(value);
        return cardMessage;
    }

    public static KOOKCardMessage newSectionWithLink(KOOKCardMessage text, KOOKCardMessage button) {
        KOOKCardMessage cardMessage = newSection(text);
        cardMessage.setMode("right");
        cardMessage.setAccessory(button);
        return cardMessage;
    }


    public static KOOKCardMessage newParagraph(int cols, List<KOOKCardMessage> fields) {
        KOOKCardMessage cardMessage = new KOOKCardMessage();
        cardMessage.setType("paragraph");
        cardMessage.setCols(cols);
        cardMessage.setFields(fields);
        return cardMessage;
    }

    public static KOOKCardMessage newCountDown(String mode, LocalDateTime startTime, LocalDateTime endTime) {
        KOOKCardMessage cardMessage = new KOOKCardMessage();
        cardMessage.setType("countdown");
        cardMessage.setMode(mode);
        cardMessage.setStartTime(startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        cardMessage.setEndTime(endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return cardMessage;
    }


    public static KOOKCardMessage quickMdSection(String content) {
        return newSection(newKMarkdown(content));
    }

    public static KOOKCardMessage quickTextLinkSection(String text, String btnName, String theme, String url) {
        return newSectionWithLink(KOOKCardMessage.newKMarkdown(text),
                KOOKCardMessage.newButton(theme, btnName, "link", url));
    }

    public static KOOKCardMessage quickBtnEventSection(String text, String btnName, String theme, String value) {
        return newSectionWithLink(KOOKCardMessage.newKMarkdown(text),
                KOOKCardMessage.newButton(theme, btnName, "return-val", value));
    }

    public static KOOKCardMessage quickContent(String content) {
        return newContext(List.of(newKMarkdown(content)));
    }

}
