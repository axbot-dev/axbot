package com.github.axiangcoding.axbot.bot.kook.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 构建Kook的KMarkdown消息
 * 消息编辑器见 <a href="https://www.kookapp.cn/tools/message-builder.html#/kmarkdown">消息编辑器</a>
 */
@Getter
@Setter
@ToString
public class KookMDMessage {
    public static String bold(String text) {
        return "**%s**".formatted(text);
    }

    public static String italic(String text) {
        return "*%s*".formatted(text);
    }

    public static String underLine(String text) {
        return "(ins)%s(ins)".formatted(text);
    }

    public static String deleteLine(String text) {
        return "~~%s~~".formatted(text);
    }

    public static String link(String link) {
        return link(link, link);
    }

    public static String link(String link, String text) {
        return "[%s](%s)".formatted(text, link);
    }

    public static String code(String text) {
        return "`%s`".formatted(text);
    }

    public static String spoiler(String text) {
        return "(spl)%s(spl)".formatted(text);
    }

    public static String codeBlock(String lang, String text) {
        return "```%s\n%s\n```\n".formatted(lang, text);
    }

    public static String quote(String text) {
        return "> %s\n\n".formatted(text);
    }

    public static String divider() {
        return "---\n";
    }

    public static String colorful(String text, String color) {
        return "(font)%s(font)[%s]\n\n".formatted(text, color);
    }
}
