package com.github.axiangcoding.axbot.app.bot.message.template;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class QGContentTemplate {
    String title;
    List<String> lines;
    String footer = "Powered By AXBot.";
    Character divider = '-';

    public QGContentTemplate(String title) {
        this.title = title;
        this.lines = new ArrayList<>();
    }

    public QGContentTemplate(String title, char divider) {
        this.title = title;
        this.divider = divider;
        this.lines = new ArrayList<>();

    }

    public void addLine(String line) {
        this.lines.add(line);
    }

    public void addDivider() {
        this.lines.add(StringUtils.repeat(divider, 13));
    }

    public String display() {
        String template = "%s\n%s\n%s";
        return template.formatted(title, StringUtils.repeat(divider, 13), StringUtils.join(lines, "\n"));
    }

    public String displayWithFooter() {
        return display() + "\n" + StringUtils.repeat(divider, 13) + "\n" + footer;
    }

    public static QGContentTemplate notSupport() {
        return new QGContentTemplate("暂不支持该功能");
    }

    public static QGContentTemplate notSupport(String content) {
        QGContentTemplate msg = notSupport();
        msg.addLine(content);
        return msg;
    }
}
