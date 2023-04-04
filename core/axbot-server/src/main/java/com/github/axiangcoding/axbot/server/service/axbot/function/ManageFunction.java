package com.github.axiangcoding.axbot.server.service.axbot.function;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.bot.kook.entity.KookCardMessage;

import java.util.List;

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
        return JSONObject.toJSONString(messages);
    }

    public static String getHelp() {
        return null;
    }
}
