package com.github.axiangcoding.axbot.app.bot.function.passive.sys;

import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.FunctionType;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import love.forte.simbot.event.ChannelMessageEvent;

@AxPassiveFunc(command = FunctionType.GET_HELP)
public class FuncGetHelp extends AbstractPassiveFunction {
    private static final String DOC_URL = "https://axbot-dev.github.io/axbot-doc/docs/document/user-manual";
    private static final String WEBSITE_URL = "https://axbot-dev.github.io/axbot-doc/";
    private static final String AUTHOR_URL = "https://space.bilibili.com/8696650";

    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        KOOKCardTemplate ct = new KOOKCardTemplate("获取帮助", "success");
        ct.addModuleContentSection("需要用@来调用v2的AXBot哦");
        ct.addModuleTextLink("查看项目主页", "项目主页", "primary", WEBSITE_URL);
        ct.addModuleTextLink("用户使用手册", "用户手册", "primary", DOC_URL);
        ct.addModuleTextLink("开发者B站首页", "开发者首页", "primary", AUTHOR_URL);
        event.replyBlocking(toCardMessage(ct.displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        QGContentTemplate ct = new QGContentTemplate("获取帮助");
        ct.addLine("需要用@来调用v2的AXBot哦");
        ct.addLine("请到官方频道查看完整文档的链接");
        event.replyBlocking(toTextMessage(ct.displayWithFooter()));
    }
}
