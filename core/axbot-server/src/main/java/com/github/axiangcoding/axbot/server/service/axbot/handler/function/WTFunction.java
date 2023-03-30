package com.github.axiangcoding.axbot.server.service.axbot.handler.function;


import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.bot.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.server.data.entity.WtGamerProfile;

import java.util.List;

public class WTFunction {
    public static String profileNotFoundMsg(String nickname, String moreMsg) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("战雷玩家 %s 的数据".formatted(nickname));
        messages.get(0).setTheme("warning");
        messages.get(0).getModules().add(KookCardMessage.newSection(KookCardMessage.newKMarkdown(moreMsg)));
        return JSONObject.toJSONString(messages);
    }

    public static String profileNotFoundMsg(String nickname) {
        return profileNotFoundMsg(nickname, "该玩家未找到");
    }

    public static String profileFound(String nickname, WtGamerProfile profile) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("战雷玩家 %s 的数据".formatted(nickname));
        messages.get(0).setTheme("success");
        messages.get(0).getModules().add(KookCardMessage.newSection(KookCardMessage.newKMarkdown(JSONObject.toJSONString(profile))));
        return JSONObject.toJSONString(messages);
    }

    public static String profileInQuery(String nickname) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("战雷玩家 %s 的数据".formatted(nickname));
        messages.get(0).setTheme("success");
        messages.get(0).getModules().add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("正在发起查询...请耐心等待")));
        return JSONObject.toJSONString(messages);
    }
}
