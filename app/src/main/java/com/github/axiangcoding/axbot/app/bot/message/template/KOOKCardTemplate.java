package com.github.axiangcoding.axbot.app.bot.message.template;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.app.bot.message.KOOKCardMessage;

import java.util.ArrayList;
import java.util.List;

public class KOOKCardTemplate {
    private final List<KOOKCardMessage> messages;

    KOOKCardMessage footer = KOOKCardMessage.quickTextLinkSection("Powered By AXBot.", "GitHub", "info", "https://github.com/axiangcoding/AXBot");

    public KOOKCardTemplate(String title, String theme) {
        this.messages = KOOKCardMessage.defaultMsg(title, theme);
    }

    public void addModule(KOOKCardMessage item) {
        List<KOOKCardMessage> modules = this.messages.get(0).getModules();
        modules.add(item);
    }

    public void addModuleDivider() {
        List<KOOKCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KOOKCardMessage.newDivider());
    }

    public void addModuleMdSection(String content) {
        List<KOOKCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KOOKCardMessage.quickMdSection(content));
    }

    public void addModuleContentSection(String content) {
        List<KOOKCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KOOKCardMessage.quickContent(content));
    }

    public void addGetHelp(String content) {
        List<KOOKCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KOOKCardMessage.quickTextLinkSection(content, "进入AXBot研究所", "primary", "https://kook.top/eUTZK7"));
    }

    public void addInviteBot(String content) {
        List<KOOKCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KOOKCardMessage.quickTextLinkSection(content, "邀请AXBot", "primary",
                "https://www.kookapp.cn/app/oauth2/authorize?id=15253&permissions=924672&client_id=eXJ0-Ntgqw-q33Oe&redirect_uri=&scope=bot"));
    }

    public String display() {
        return JSONObject.toJSONString(messages);
    }

    public String displayWithFooter() {
        ArrayList<KOOKCardMessage> tempList = new ArrayList<>(this.messages);
        List<KOOKCardMessage> modules = tempList.get(0).getModules();
        if (this.footer != null) {
            modules.add(KOOKCardMessage.newDivider());

            modules.add(this.footer);
        }
        return JSONObject.toJSONString(tempList);
    }
}
