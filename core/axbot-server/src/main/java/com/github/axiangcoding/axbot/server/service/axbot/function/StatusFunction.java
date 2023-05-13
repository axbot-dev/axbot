package com.github.axiangcoding.axbot.server.service.axbot.function;

import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.util.JsonUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatusFunction {
    public static String guildSettingFound(KookGuildSetting setting) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("当前服务器状态", "success");
        List<KookCardMessage> modules = messages.get(0).getModules();
        ArrayList<KookCardMessage> fields = new ArrayList<>();
        String template = "%s:\n%s";

        Map<String, Object> map = new LinkedHashMap<>();
        KookGuildSetting.FunctionSetting functionSetting = setting.getFunctionSetting();
        map.put("服务器ID", setting.getGuildId());
        map.put("服务器状态", setting.getBanned() ? "已拉黑" : "正常");
        map.put("战雷新闻播报状态", functionSetting.getEnableWtNewsReminder() ? "开启" : "关闭");
        map.put("战雷新闻播报频道", functionSetting.getWtNewsChannelId());
        map.put("B站直播通知状态", functionSetting.getEnableBiliLiveReminder() ? "开启" : "关闭");
        map.put("B站直播间", functionSetting.getBiliRoomId());
        map.put("B站直播通知频道", functionSetting.getBiliLiveChannelId());
        // map.put("禁用状态", setting.getBanned());

        map.forEach((k, v) -> {
            fields.add(KookCardMessage.newKMarkdown(template.formatted(KookMDMessage.bold(k), v)));
        });

        modules.add(KookCardMessage.newSection(KookCardMessage.newParagraph(2, fields)));

        return JsonUtils.toJson(messages);
    }

    public static String guildSettingNotFound() {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("当前服务器状态", "danger");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("未找到本服务器的状态！")));

        return JsonUtils.toJson(messages);
    }

    public static String userSettingFound(KookUserSetting setting) {
        // List<KookCardMessage> messages = KookCardMessage.defaultMsg("当前服务器状态", "success");
        // List<KookCardMessage> modules = messages.get(0).getModules();
        // ArrayList<KookCardMessage> fields = new ArrayList<>();
        // String template = "%s:\n%s";
        //
        // Map<String, Object> map = new LinkedHashMap<>();
        // KookUserSetting.Permit permit = setting.getPermit();
        // UserUsage usage = setting.getUsage();
        // map.put("用户ID", setting.getUserId());
        // map.put("用户状态", setting.getBanned() ? "已拉黑" : "正常");
        // map.put("授权状态 - AI聊天功能", permit.getCanUseAI() ? "开启" : "关闭");
        // map.put("使用情况 - 当日命令请求", "%d/%d".formatted(usage.getInputToday(), KookUserSetting.INPUT_LIMIT));
        // map.put("使用情况 - 总共命令请求", usage.getInputTotal());
        //
        // map.forEach((k, v) -> {
        //     fields.add(KookCardMessage.newKMarkdown(template.formatted(KookMDMessage.bold(k), v)));
        // });
        //
        // modules.add(KookCardMessage.newSection(KookCardMessage.newParagraph(2, fields)));
        //
        return null;
    }

    public static String userSettingNotFound() {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("当前个人状态", "danger");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("未找到您的状态！")));

        return JsonUtils.toJson(messages);
    }
}
