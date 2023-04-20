package com.github.axiangcoding.axbot.server.service.axbot.function;

import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.server.util.JsonUtils;

import java.util.List;
import java.util.Random;

public class LuckyFunction {

    public static String todayLuckyForKook(long seed) {
        int luck = new Random(seed).nextInt(100);
        String title = "您的今日气运为：" + luck;
        List<KookCardMessage> messages = KookCardMessage.defaultMsg(title, "success");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown(genExtra(luck))));

        return JsonUtils.toJson(messages);
    }

    public static String todayLuckForCqhttp(long seed) {
        int luck = new Random(seed).nextInt(100);
        return "您的今日气运为：%d，%s".formatted(luck, genExtra(luck));
    }

    private static String genExtra(int luck) {
        if (luck == 0) {
            return "好一个0！";
        } else if (luck < 30) {
            return "您就是非洲酋长失散多年的异父异母的亲兄弟";
        } else if (luck < 60) {
            return "凑合凑合日子还得过";
        } else if (luck < 80) {
            return "老欧洲人了，那叫一个地~道";
        } else if (luck < 100) {
            return "来一发狠狠奖励自己！";
        } else if (luck == 100) {
            return "到达最高分，理塘！";
        } else {
            return "";
        }
    }
}