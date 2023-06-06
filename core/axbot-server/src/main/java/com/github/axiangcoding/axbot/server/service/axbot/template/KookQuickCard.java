package com.github.axiangcoding.axbot.server.service.axbot.template;

import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.server.util.JsonUtils;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class KookQuickCard {
    private final List<KookCardMessage> messages;

    @Setter
    KookCardMessage footer = KookCardMessage.quickTextLinkSection("Powered By AXBot.", "GitHub", "info", "https://github.com/axiangcoding/AXBot");

    public KookQuickCard(String title, String theme) {
        this.messages = KookCardMessage.defaultMsg(title, theme);
    }

    public void addModule(KookCardMessage item) {
        List<KookCardMessage> modules = this.messages.get(0).getModules();
        modules.add(item);
    }

    public void addModuleDivider() {
        List<KookCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KookCardMessage.newDivider());
    }

    public void addModuleMdSection(String content) {
        List<KookCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KookCardMessage.quickMdSection(content));
    }

    public void addModuleContentSection(String content) {
        List<KookCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KookCardMessage.quickContent(content));
    }

    public void addModuleGetHelp(String content) {
        List<KookCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KookCardMessage.quickTextLinkSection(content, "进入AXBot研究所", "primary", "https://kook.top/eUTZK7"));
    }

    public void addModuleInviteBot(String content) {
        List<KookCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KookCardMessage.quickTextLinkSection(content, "邀请AXBot", "primary",
                "https://www.kookapp.cn/app/oauth2/authorize?id=15253&permissions=924672&client_id=eXJ0-Ntgqw-q33Oe&redirect_uri=&scope=bot"));
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
