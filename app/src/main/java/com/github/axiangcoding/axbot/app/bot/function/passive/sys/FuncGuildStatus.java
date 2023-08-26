package com.github.axiangcoding.axbot.app.bot.function.passive.sys;

import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.enums.FunctionType;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.KOOKMDMessage;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import com.github.axiangcoding.axbot.app.server.data.entity.EndGuild;
import com.github.axiangcoding.axbot.app.server.data.entity.field.EndGuildStatus;
import com.github.axiangcoding.axbot.app.server.data.entity.field.EndGuildUsage;
import com.github.axiangcoding.axbot.app.server.service.EndGuildService;
import jakarta.annotation.Resource;
import love.forte.simbot.event.ChannelMessageEvent;

import java.util.LinkedHashMap;
import java.util.Map;

@AxPassiveFunc(command = FunctionType.GUILD_STATUS)
public class FuncGuildStatus extends AbstractPassiveFunction {
    @Resource
    private EndGuildService endGuildService;


    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        String guildId = getGuildId(event);
        EndGuild endGuild = endGuildService.getOrCreate(guildId, BotPlatform.KOOK);
        LinkedHashMap<String, Object> map = getMap(endGuild);

        KOOKCardTemplate ct = new KOOKCardTemplate("当前服务器状态", "success");

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            ct.addModuleMdSection("%s: %s".formatted(KOOKMDMessage.bold(entry.getKey()),
                    KOOKMDMessage.code(entry.getValue().toString())));
        }
        event.replyBlocking(toCardMessage(ct.displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        String guildId = getGuildId(event);
        EndGuild endGuild = endGuildService.getOrCreate(guildId, BotPlatform.QQ_GUILD);
        LinkedHashMap<String, Object> map = getMap(endGuild);

        QGContentTemplate ct = new QGContentTemplate("当前服务器状态");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            ct.addLine("%s: %s".formatted(entry.getKey(), entry.getValue()));
        }
        event.replyBlocking(toTextMessage(ct.displayWithFooter()));
    }

    private LinkedHashMap<String, Object> getMap(EndGuild endGuild) {
        String guildId = endGuild.getGuildId();
        EndGuildUsage usage = endGuild.getUsage();
        EndGuildStatus status = endGuild.getStatus();

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("服务器ID", guildId);
        map.put("当前状态", status.getStatus());
        map.put("今日输入次数", usage.getInputToday());
        map.put("全部输入次数", usage.getInputTotal());
        map.put("今日请求战雷次数", usage.getQueryWtToday());
        map.put("全部请求战雷次数", usage.getQueryWtTotal());
        return map;
    }
}
