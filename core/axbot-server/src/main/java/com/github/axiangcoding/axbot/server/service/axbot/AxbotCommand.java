package com.github.axiangcoding.axbot.server.service.axbot;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum AxbotCommand {
    COMMAND_HELP(1, Arrays.asList("帮助", "文档", "help")),
    COMMAND_VERSION(2, Arrays.asList("版本", "version")),
    COMMAND_LUCKY(3, Arrays.asList("气运", "运气", "luck"));

    private final Integer index;
    private final List<String> texts;

    AxbotCommand(int index, List<String> texts) {
        this.index = index;
        this.texts = texts;
    }
}
