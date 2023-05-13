package com.github.axiangcoding.axbot.engine.v1;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public enum InteractiveCommand {
    COMMAND_DEFAULT(null, null),
    COMMAND_HELP(List.of("帮助", "文档", "help"), null),
    COMMAND_VERSION(List.of("版本", "version"), null),
    COMMAND_LUCKY(List.of("气运", "运气", "luck"), null),
    COMMAND_WT_QUERY_PROFILE(List.of("战雷", "战争雷霆", "wt"), List.of("查询", "查找", "query")),
    COMMAND_WT_UPDATE_PROFILE(List.of("战雷", "战争雷霆", "wt"), List.of("更新", "刷新", "update")),
    COMMAND_GUILD_STATUS(List.of("群状态", "服务器状态", "群组状态", "serverStatus"), null),
    COMMAND_USER_STATUS(List.of("状态", "我的状态", "个人状态", "status"), null),
    COMMAND_GUILD_MANAGE(List.of("管理", "社群管理", "群组管理", "manage"), null),
    COMMAND_CHAT_WITH_AI(List.of("聊天", "对话", "chat"), null),
    COMMAND_SUBSCRIBE(List.of("赞助", "订阅", "subscribe"), null);

    private final List<String> t1;
    private final List<String> t2;

    public static InteractiveCommand judgeCommand(String command) {
        if (StringUtils.isBlank(command)) {
            return COMMAND_DEFAULT;
        }
        String[] c = StringUtils.split(command);
        for (InteractiveCommand value : InteractiveCommand.values()) {
            if (value.t1 != null) {
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
        }
        return COMMAND_DEFAULT;
    }

    public static String[] getParamList(String command) {
        String[] split = StringUtils.split(command);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(split));
        InteractiveCommand judgeCommand = judgeCommand(command);
        if (judgeCommand.t1 != null) {
            list.remove(0);
        }
        if (judgeCommand.t2 != null) {
            list.remove(0);
        }
        return list.toArray(new String[0]);
    }
}
