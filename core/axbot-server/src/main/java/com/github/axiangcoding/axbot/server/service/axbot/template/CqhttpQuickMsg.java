package com.github.axiangcoding.axbot.server.service.axbot.template;

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

    public String display() {
        return template.formatted(title, StringUtils.join(lines, "\n"));
    }

    public String displayWithFooter() {
        return display();
        // QQ受到字数限制，不再展示页脚
        // return display() + "-------------\n" + footer;
    }
}
