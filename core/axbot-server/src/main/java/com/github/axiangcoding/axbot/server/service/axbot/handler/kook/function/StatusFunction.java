package com.github.axiangcoding.axbot.server.service.axbot.handler.kook.function;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.bot.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;

import java.util.ArrayList;
import java.util.List;

public class StatusFunction {
    public static String settingFound(KookGuildSetting setting) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("当前服务器状态", "success");
        List<KookCardMessage> modules = messages.get(0).getModules();
        ArrayList<KookCardMessage> elements = new ArrayList<>();
        elements.add(KookCardMessage.newKMarkdown("服务器ID: %s".formatted(setting.getGuildId())));
        elements.add(KookCardMessage.newKMarkdown("是否被禁用: %s".formatted(setting.getBanned())));

        modules.add(KookCardMessage.newContext(elements));

        return JSONObject.toJSONString(messages);
    }

    public static String settingNotFound() {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("当前服务器状态", "danger");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("未找到本服务器的状态！")));

        return JSONObject.toJSONString(messages);
    }
}
