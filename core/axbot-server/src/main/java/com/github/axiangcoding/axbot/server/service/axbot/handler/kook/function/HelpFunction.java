package com.github.axiangcoding.axbot.server.service.axbot.handler.kook.function;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.bot.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.bot.kook.entity.KookKMarkdownMessage;

import java.util.LinkedHashMap;
import java.util.List;

public class HelpFunction {

    public static String helpCard() {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("AXBot 帮助手册", "success");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newHeader("常用命令"));
        modules.add(KookCardMessage.newContext(List.of(KookCardMessage.newPlainText("以形如 “axbot [命令] [参数]”的格式调用"))));

        commandDescription().forEach((k, v) -> {
            String msg = KookKMarkdownMessage.code(k) + " - " + v;
            modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown(msg)));
        });

        modules.add(KookCardMessage.newDivider());
        modules.add(KookCardMessage.newHeader("完整命令"));
        modules.add(KookCardMessage.newSectionWithLink(
                KookCardMessage.newKMarkdown("上面列出的只是常用命令的常用调用形式，更多调用方式请点击按钮跳转文档查看~"),
                KookCardMessage.newButton("info", "跳转到文档")));

        modules.add(KookCardMessage.newDivider());
        modules.add(KookCardMessage.newSection(
                KookCardMessage.newKMarkdown("(font)更多功能正在开发中！(font)[warning]")));
        return JSONObject.toJSONString(messages);
    }

    private static LinkedHashMap<String, String> commandDescription() {
        LinkedHashMap<String, String> commandMap = new LinkedHashMap<>();
        commandMap.put("axbot 气运", "获取今天的气运值");
        commandMap.put("axbot 战雷 查询 [玩家昵称]", "查询战雷的玩家数据");
        commandMap.put("axbot 战雷 刷新 [玩家昵称]", "刷新战雷的玩家数据");
        commandMap.put("axbot 帮助", "获取帮助手册");
        commandMap.put("axbot 状态", "查看当前所处的Kook服务器的信息");
        commandMap.put("axbot 版本", "获取当前机器人的部署版本");
        return commandMap;
    }
}
