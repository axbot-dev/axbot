package com.github.axiangcoding.axbot.server.service.axbot.template;

import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.server.util.JsonUtils;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class KookQuickCard {
    private final List<KookCardMessage> messages;

    @Setter
    KookCardMessage footer = KookCardMessage.quickTextLinkSection("Powered By AXBot.", "关注开发者", "info", "https://space.bilibili.com/8696650");

    public KookQuickCard(String title, String theme) {
        this.messages = KookCardMessage.defaultMsg(title, theme);
    }

    public void addModule(KookCardMessage item) {
        List<KookCardMessage> modules = this.messages.get(0).getModules();
        modules.add(item);
    }

    public String display() {
        return JsonUtils.toJson(messages);
    }

    public String displayWithFooter() {
        ArrayList<KookCardMessage> tempList = new ArrayList<>(this.messages);
        List<KookCardMessage> modules = tempList.get(0).getModules();
        if (this.footer != null) {
            modules.add(KookCardMessage.newDivider());
            modules.add(this.footer);
        }
        return JsonUtils.toJson(tempList);
    }

}
