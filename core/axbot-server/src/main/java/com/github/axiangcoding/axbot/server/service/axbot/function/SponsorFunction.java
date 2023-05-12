package com.github.axiangcoding.axbot.server.service.axbot.function;

import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.util.JsonUtils;

import java.time.LocalDateTime;
import java.util.List;

public class SponsorFunction {
    public static String getPanel(String orderId) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("你好，我是AxBot", "success");
        List<KookCardMessage> modules = messages.get(0).getModules();

        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("如果您选择赞助AxBot，我们感激不尽。")));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("点击以下链接将跳转到爱发电平台发起赞助，如果选择了带订阅的方案，会自动激活到当前账号或者当前社群中")));
        modules.add(KookCardMessage.newDivider());

        String url = "https://afdian.net/order/create?plan_id=bf8dd888eed711eda90b52540025c377&custom_order_id=%s";


        modules.add(KookCardMessage.newSectionWithLink(KookCardMessage.newKMarkdown("赞助个人版普通订阅（当前账号）"),
                KookCardMessage.newButton("primary", "个人普通订阅", "link", url.formatted(orderId))));
        modules.add(KookCardMessage.newSectionWithLink(KookCardMessage.newKMarkdown("仅赞助（会展示在赞助名单中，无特权）"),
                KookCardMessage.newButton("primary", "仅赞助", "link", "https://afdian.net/order/create?user_id=966767508b5811eca47c52540025c377")));
        LocalDateTime now = LocalDateTime.now();
        modules.add(KookCardMessage.newCountDown("second", now, now.plusMinutes(10)));
        modules.add(KookCardMessage.newDivider());
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("如果赞助过程中发生了任何问题，请联系开发者处理")));
        modules.add(KookCardMessage.newSectionWithLink(KookCardMessage.newKMarkdown("加入Kook服务器，到#赞助相关频道进行反馈"),
                KookCardMessage.newButton("info", "点击跳转", "link", "https://kook.top/eUTZK7")));
        modules.add(KookCardMessage.newSectionWithLink(KookCardMessage.newKMarkdown("私信开发者"),
                KookCardMessage.newButton("info", "点击跳转", "link", "https://www.kookapp.cn/app/home/privatemessage/2936837460")));
        return JsonUtils.toJson(messages);
    }

    public static String sponsorSuccess(int month, String planName) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("你好，我是AxBot", "success");
        List<KookCardMessage> modules = messages.get(0).getModules();
        String content = "感谢您赞助了 %s 个月的 %s".formatted(month, KookMDMessage.code(planName));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown(content)));
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("如果你想了解自己的订阅状态，请使用命令 `axbot 状态` 查看")));
        return JsonUtils.toJson(messages);
    }
}
