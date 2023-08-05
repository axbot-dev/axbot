package com.github.axiangcoding.axbot.app.bot.enums;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public enum UserCmd {
    /*
    以下是系统类的交互
     */
    DEBUG(List.of("调试", "debug"), null),
    GET_HELP(List.of("帮助", "文档", "help"), null),
    VERSION(List.of("版本", "version"), null),
    GUILD_STATUS(List.of("服务器状态", "群组状态", "guildStatus"), null),
    USER_STATUS(List.of("状态", "我的状态", "个人状态", "status"), null),
    SPONSOR(List.of("赞助", "订阅", "subscribe"), null),
    GUILD_MANAGE(List.of("管理", "社群管理", "群组管理", "manage"), null),
    BUG_REPORT(List.of("反馈", "bug", "bug反馈", "bugReport"), null),

    /*
    以下是游戏类的交互
     */
    WT_QUERY_PROFILE(List.of("战雷", "战争雷霆", "wt"), List.of("查询", "查找", "query")),
    WT_QUERY_HISTORY(List.of("战雷", "战争雷霆", "wt"), List.of("历史", "历史记录", "history")),
    WT_UPDATE_PROFILE(List.of("战雷", "战争雷霆", "wt"), List.of("更新", "刷新", "update")),
    WT_BIND_PROFILE(List.of("战雷", "战争雷霆", "wt"), List.of("绑定", "快捷绑定", "bind")),
    WT_UNBIND_PROFILE(List.of("战雷", "战争雷霆", "wt"), List.of("解绑", "快捷解绑", "unbind")),
    WT_REPORT_GAMER(List.of("战雷", "战争雷霆", "wt"), List.of("举办", "举报", "举报玩家", "report")),

    /*
    以下是娱乐类的交互
     */
    LUCKY_TODAY(List.of("气运", "运气", "luck"), null),
    DRAW_CARD(List.of("抽卡", "drawCard"), null),
    CHAT_WITH_AI(List.of("聊天", "对话", "chat"), null),


    /*
    以下均为拦截式交互，不是对特定命令的响应，而是在特定情况下拦截原有的交互，返回一些信息
     */
    DEFAULT(null, null),
    CENSOR_FAILED(null, null),
    USER_BANNED(null, null),
    GUILD_BANNED(null, null),
    GUILD_USAGE_LIMIT(null, null),
    USER_USAGE_LIMIT(null, null),
    ERROR(null, null),
    ;

    private final List<String> t1;
    private final List<String> t2;

    public String toCommand() {
        StringBuilder sb = new StringBuilder();
        if (t1 != null) {
            sb.append(t1.get(0));
        }
        if (t2 != null) {
            sb.append(" ");
            sb.append(t2.get(0));
        }
        return sb.toString();
    }

    public static UserCmd judgeCommand(String command) {
        if (StringUtils.isBlank(command)) {
            return DEFAULT;
        }
        String[] c = StringUtils.split(StringUtils.trim(command));
        for (UserCmd value : UserCmd.values()) {
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
        return DEFAULT;
    }

    public static String[] getParamList(String command) {
        String[] split = StringUtils.split(command);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(split));
        UserCmd judgeCommand = judgeCommand(command);
        if (judgeCommand.t1 != null) {
            list.remove(0);
        }
        if (judgeCommand.t2 != null) {
            list.remove(0);
        }
        return list.toArray(new String[0]);
    }
}
