package com.github.axiangcoding.axbot.app.bot.function.active;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.app.bot.annotation.AxActiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.ActiveEvent;
import com.github.axiangcoding.axbot.app.bot.enums.ClickBtnEvent;
import com.github.axiangcoding.axbot.app.bot.function.AbstractActiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.KOOKCardMessage;
import com.github.axiangcoding.axbot.app.bot.message.KOOKMDMessage;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import com.github.axiangcoding.axbot.app.crawler.entity.WTNewParseResult;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.Identifies;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.component.qguild.QGBot;
import love.forte.simbot.definition.Channel;
import love.forte.simbot.definition.Guild;

import java.util.HashMap;
import java.util.Map;

@AxActiveFunc(event = ActiveEvent.REMIND_WT_NEWS)
@Slf4j
public class FuncRemindWtNews extends AbstractActiveFunction {
    @Override
    public void processByKOOK(Bot bot, Map<String, Object> params) {
        WTNewParseResult parseResult = (WTNewParseResult) params.get("item");
        String guildId = (String) params.get("guildId");
        String channelId = (String) params.get("channelId");
        String atRoleId = (String) params.get("atRoleId");
        String title = parseResult.getTitle();
        String comment = parseResult.getComment();
        String dateStr = parseResult.getDateStr();
        String url = parseResult.getUrl();


        Map<String, String> map = new HashMap<>();
        map.put("type", ClickBtnEvent.GET_ROLE.name());
        map.put("role_id", atRoleId);

        KOOKCardTemplate ct = new KOOKCardTemplate("AXBot为各位带来战雷的最新资讯", "info");
        ct.addModule(KOOKCardMessage.newHeader("%s".formatted(title)));
        ct.addModuleContentSection(dateStr);
        ct.addModuleMdSection(comment);
        if (atRoleId != null) {
            ct.addModuleDivider();
            ct.addModuleMdSection(KOOKMDMessage.role(atRoleId));
        }
        ct.addModuleDivider();
        ct.addModuleTextLink("点击跳转到官网查看原文", "跳转官网", "primary", url);
        ct.addModuleBtnEvent("如果你也要获得实时更新，可以获取该身份",
                "获取身份", "info", JSONObject.toJSONString(map));
        Guild guild = bot.getGuild(Identifies.ID(guildId));
        if (guild != null) {
            Channel channel = guild.getChannel(Identifies.ID(channelId));
            if (channel != null) {
                channel.sendBlocking(toCardMessage(ct.displayWithFooter()));
            } else {
                log.error("channel {} not found", channelId);
            }
        } else {
            log.error("guild {} not found", guildId);
        }
    }

    @Override
    public void processByQG(Bot bot, Map<String, Object> params) {
        WTNewParseResult parseResult = (WTNewParseResult) params.get("item");
        String guildId = (String) params.get("guildId");
        String channelId = (String) params.get("channelId");
        String atRoleId = (String) params.get("atRoleId");
        String title = parseResult.getTitle();
        String comment = parseResult.getComment();
        String dateStr = parseResult.getDateStr();
        String url = parseResult.getUrl();
        QGContentTemplate ct = new QGContentTemplate("AXBot为各位带来战雷的最新资讯");
        ct.addLine(title);
        ct.addLine(dateStr);
        ct.addLine(comment);
        ((QGBot) bot).sendToBlocking(Identifies.ID(channelId), toTextMessage(ct.displayWithFooter()));
    }
}
