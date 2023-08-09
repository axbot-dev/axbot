package com.github.axiangcoding.axbot.app.bot.function.passive.inject;

import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.enums.FunctionType;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.KOOKMDMessage;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import com.github.axiangcoding.axbot.app.server.data.entity.EndGuild;
import com.github.axiangcoding.axbot.app.server.service.EndGuildService;
import jakarta.annotation.Resource;
import love.forte.simbot.event.ChannelMessageEvent;

@AxPassiveFunc(command = FunctionType.GUILD_USAGE_LIMIT)
public class FuncGuildUsageLimit extends AbstractPassiveFunction {

    @Resource
    EndGuildService endGuildService;

    @Resource
    FuncGuildBanned funcGuildBanned;

    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        String guildId = event.getSource().getGuild().getId().toString();
        EndGuild endGuild = endGuildService.getOrCreate(guildId, BotPlatform.KOOK);
        Integer inputToday = endGuild.getUsage().getInputToday();
        int inputLimit = endGuildService.getInputLimit(guildId, BotPlatform.KOOK);
        if (inputToday > inputLimit + 5) {
            endGuildService.blockGuild(guildId, BotPlatform.KOOK, "使用超出限制5次以上");
            funcGuildBanned.processForKOOK(event);
            return;
        }
        KOOKCardTemplate ct = new KOOKCardTemplate("使用超限", "warning");
        ct.addModuleMdSection("本服务器在今日使用的次数已经超限，第二天重置");
        ct.addModuleMdSection("当日使用：%s / %s".formatted(
                KOOKMDMessage.code(String.valueOf(inputToday)), KOOKMDMessage.code(String.valueOf(inputLimit))));
        ct.addModuleMdSection("超限5次会被拉黑，请谨慎使用命令");
        event.replyBlocking(toCardMessage(ct.displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        String guildId = event.getSource().getGuild().getId().toString();
        EndGuild endGuild = endGuildService.getOrCreate(guildId, BotPlatform.QQ_GUILD);
        Integer inputToday = endGuild.getUsage().getInputToday();
        int inputLimit = endGuildService.getInputLimit(guildId, BotPlatform.QQ_GUILD);
        if (inputToday > inputLimit + 5) {
            endGuildService.blockGuild(guildId, BotPlatform.QQ_GUILD, "使用超出限制5次以上");
            funcGuildBanned.processForQG(event);
            return;
        }
        QGContentTemplate ct = new QGContentTemplate("使用超限");
        ct.addLine("本服务器在今日使用的次数已经超限，第二天重置");
        ct.addLine("当日使用：%s / %s".formatted(
                String.valueOf(inputToday), String.valueOf(inputLimit)));
        ct.addLine("超限5次会被拉黑，请谨慎使用命令");
        event.replyBlocking(toTextMessage(ct.displayWithFooter()));
    }
}
