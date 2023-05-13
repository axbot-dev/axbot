package com.github.axiangcoding.axbot.server.service.axbot.function.interactive;

import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.util.JsonUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 */
public class ManageFunction {

    public static String noPermission(String nickname) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("您好，%s".formatted(nickname), "danger");
        List<KookCardMessage> modules = messages.get(0).getModules();
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("您的账号未具备管理员权限，因此您无法管理AXBot！")));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("AXBot仅接受本服务器中具备管理员权限角色的用户的管理指令")));
        modules.add(KookCardMessage.newDivider());
        modules.add(KookCardMessage.newContext(List.of(
                KookCardMessage.newKMarkdown("如果您是服务器所有者，请检查是否给自己分配了具备管理员权限的角色")
        )));
        return JsonUtils.toJson(messages);
    }

    public static String botHaveNoPermission(String nickname) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("您好，%s".formatted(nickname), "danger");
        List<KookCardMessage> modules = messages.get(0).getModules();
        modules.add(KookCardMessage.newHeader("配置失败！"));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("AXBot可能未具备具有`管理角色权限`权限的角色身份，请赋予后重试")));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("如果问题一直存在请通知开发者")));
        return JsonUtils.toJson(messages);
    }

    public static String getHelp(String nickname, String cmdPrefix) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("您好，%s".formatted(nickname), "info");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newHeader("可用的管理命令"));
        modules.add(KookCardMessage.newContext(List.of(KookCardMessage.newKMarkdown("请严格按照格式输入命令，否则命令不会被识别"))));

        commandDescription(cmdPrefix).forEach((k, v) -> {
            modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown(
                    KookMDMessage.code(k) + " - " + v)));
        });

        return JsonUtils.toJson(messages);
    }

    public static String configSuccess(String nickname, Map<String, Object> items) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("您好，%s".formatted(nickname), "success");
        List<KookCardMessage> modules = messages.get(0).getModules();
        modules.add(KookCardMessage.newHeader("配置修改成功"));

        items.forEach((k, v) -> {
            modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown(
                    KookMDMessage.bold(k) + ": " + v)));
        });

        return JsonUtils.toJson(messages);
    }

    public static String configError(String nickname) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("您好，%s".formatted(nickname), "danger");
        List<KookCardMessage> modules = messages.get(0).getModules();
        modules.add(KookCardMessage.newHeader("配置修改失败"));

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("请检查配置字段是否正确！")));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("如果你不知道要怎么开始，请查看帮助")));

        return JsonUtils.toJson(messages);
    }

    private static Map<String, String> commandDescription(String prefix) {
        Map<String, String> cmds = new LinkedHashMap<>();
        cmds.put("%s 管理 B站直播通知 <开|关> [直播间号]".formatted(prefix), "配置B站直播通知的开关。设置为开时，则在当前频道通知");
        cmds.put("%s 管理 战雷新闻播报 <开|关>".formatted(prefix), "配置战雷新闻播报开关。设置为开时，则在当前频道播报");
        cmds.put("%s 管理 战雷战绩查询 <开|关>".formatted(prefix), "配置战雷战绩查询开关。设置为开时，则在当前频道允许查询");

        return cmds;
    }
}
