package com.github.axiangcoding.axbot.engine;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@AllArgsConstructor
public enum AxbotCommand {
    COMMAND_HELP(List.of("帮助", "文档", "help"), null),
    COMMAND_VERSION(List.of("版本", "version"), null),
    COMMAND_LUCKY(List.of("气运", "运气", "luck"), null),
    COMMAND_WT_QUERY_PROFILE(List.of("战雷", "战争雷霆", "wt"), List.of("查询", "查找", "query")),
    COMMAND_WT_UPDATE_PROFILE(List.of("战雷", "战争雷霆", "wt"), List.of("更新", "刷新", "update")),
    COMMAND_GUILD_STATUS(List.of("状态", "社群状态", "群组状态", "status"), null),
    COMMAND_GUILD_MANAGE(List.of("管理", "社群管理", "群组管理", "manage"), null)
    ;

    private final List<String> t1;
    private final List<String> t2;

    public static AxbotCommand judgeCommand(String command) {
        if (StringUtils.isBlank(command)) {
            return null;
        }

        String[] c = StringUtils.split(command);
        for (AxbotCommand value : AxbotCommand.values()) {
            if (value.t1.contains(c[0])) {
                if (value.t2 != null) {
                    if (value.t2.contains(c[1])) {
                        return value;
                    }
                } else {
                    return value;
                }
            }
        }
        return null;
    }
}
