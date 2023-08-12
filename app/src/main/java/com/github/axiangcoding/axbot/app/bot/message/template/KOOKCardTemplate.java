package com.github.axiangcoding.axbot.app.bot.message.template;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.app.bot.message.KOOKCardMessage;
import com.github.axiangcoding.axbot.app.bot.message.KOOKMDMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KOOKCardTemplate {
    private final List<KOOKCardMessage> messages;

    KOOKCardMessage footer = KOOKCardMessage.quickTextLinkSection("Powered By AXBot.", "GitHub", "info", "https://github.com/axbot-dev/axbot");

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

    public void addModuleHeader(String header) {
        List<KOOKCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KOOKCardMessage.newHeader(header));
    }

    public void addModuleContentSection(String content) {
        List<KOOKCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KOOKCardMessage.quickContent(content));
    }

    public void addModuleTextLink(String text, String btnName, String theme, String url) {
        List<KOOKCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KOOKCardMessage.quickTextLinkSection(text, btnName, theme, url));
    }

    public void addModuleBtnEvent(String text, String btnName, String theme, String event) {
        List<KOOKCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KOOKCardMessage.quickBtnEventSection(text, btnName, theme, event));
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

    public void addCountDown(String mode, LocalDateTime start, LocalDateTime end) {
        List<KOOKCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KOOKCardMessage.newCountDown(mode, start, end));
    }

    public void addKVLine(String key, String value) {
        List<KOOKCardMessage> modules = this.messages.get(0).getModules();
        modules.add(KOOKCardMessage.quickMdSection("%s: %s".formatted(KOOKMDMessage.bold(key), value)));
    }

    @Deprecated
    public void addKVParagraph(LinkedHashMap<String, Object> map, Integer size) {
        List<KOOKCardMessage> modules = this.messages.get(0).getModules();
        String template = "%s:\n%s".formatted(KOOKMDMessage.bold("%s"), KOOKMDMessage.code("%s"));
        ArrayList<KOOKCardMessage> fields = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            fields.add(KOOKCardMessage.newKMarkdown(template.formatted(
                    entry.getKey(), entry.getValue())));
        }
        modules.add(KOOKCardMessage.newSection(KOOKCardMessage.newParagraph(size, fields)));
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
