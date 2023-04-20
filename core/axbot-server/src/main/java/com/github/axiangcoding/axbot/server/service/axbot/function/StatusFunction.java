package com.github.axiangcoding.axbot.server.service.axbot.function;

import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import com.github.axiangcoding.axbot.server.util.JsonUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatusFunction {
    public static String settingFound(KookGuildSetting setting) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("当前服务器状态", "success");
        List<KookCardMessage> modules = messages.get(0).getModules();
        ArrayList<KookCardMessage> fields = new ArrayList<>();
        String template = "%s:\n%s";

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("服务器ID", setting.getGuildId());
        // map.put("禁用状态", setting.getBanned());

        map.forEach((k, v) -> {
            fields.add(KookCardMessage.newKMarkdown(template.formatted(KookMDMessage.bold(k), v)));
        });

        modules.add(KookCardMessage.newSection(KookCardMessage.newParagraph(2, fields)));

        return JsonUtils.toJson(messages);
    }

    public static String settingNotFound() {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("当前服务器状态", "danger");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("未找到本服务器的状态！")));

        return JsonUtils.toJson(messages);
    }
}