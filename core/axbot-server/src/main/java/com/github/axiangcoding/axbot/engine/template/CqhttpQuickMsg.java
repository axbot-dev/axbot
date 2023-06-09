package com.github.axiangcoding.axbot.engine.template;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CqhttpQuickMsg {
    String title;
    List<String> lines;
    String footer = "Powered By AXBot.";

    private static final String template = """
            %s
            -------------
            %s
            """;

    public CqhttpQuickMsg(String title) {
        this.title = title;
        this.lines = new ArrayList<>();
    }

    public void addLine(String line) {
        this.lines.add(line);
    }

    public void addDivider() {
        this.lines.add("-------------");
    }

    public void addDivider(char override) {
        this.lines.add(StringUtils.repeat(override, 13));
    }

    public String display() {
        return template.formatted(title, StringUtils.join(lines, "\n"));
    }

    public String displayWithFooter() {
        return display() + "-------------\n" + footer;
    }

    public String displayWithFooter(char override) {
        return display() + StringUtils.repeat(override, 13) + "\n" + footer;
    }

    public static CqhttpQuickMsg notSupport() {
        return new CqhttpQuickMsg("暂不支持该功能");
    }
}
