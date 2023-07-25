package com.github.axiangcoding.axbot.app.bot.function.active;

import com.github.axiangcoding.axbot.app.bot.annotation.AxActiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.ActiveEvent;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.function.AbstractActiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.EndGuildStatus;
import com.github.axiangcoding.axbot.app.server.service.EndGuildService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.Identifies;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.definition.Channel;
import love.forte.simbot.definition.Guild;

import java.util.Map;

@AxActiveFunc(event = ActiveEvent.JOIN_GUILD)
@Slf4j
public class FuncJoinGuild extends AbstractActiveFunction {
    @Resource
    EndGuildService endGuildService;

    @Override
    public void processByKOOK(Bot bot, Map<String, Object> params) {
        String guildId = params.get("guild_id").toString();
        endGuildService.updateStatus(BotPlatform.KOOK, guildId,
                EndGuildStatus.STATUS.NORMAL);
        log.info("bot at platform {} join guild {}", BotPlatform.KOOK.name(), guildId);

        Guild guild = bot.getGuild(Identifies.ID(guildId));
        if (guild != null) {
            Channel channel = guild.getChannels().limit(1).collectToList().get(0);
            channel.sendBlocking(toCardMessage(KookWelcome().displayWithFooter()));
        }

    }

    private KOOKCardTemplate KookWelcome() {
        KOOKCardTemplate ct = new KOOKCardTemplate("大家好，我是AXBot v2", "success");
        ct.addModuleMdSection("我是一个机器人，初次见面，请多多关照！");
        ct.addModuleMdSection("你可以@我，并使用命令触发我的功能。");
        ct.addModuleMdSection("如果你不知道怎么开始，请@我，并输入“帮助”");
        ct.addModuleDivider();
        ct.addModuleMdSection("我和AXBot V1是不同的机器人哦，如果你想使用V1版本的axbot，请使用axbot 帮助");
        return ct;
    }

    @Override
    public void processByQG(Bot bot, Map<String, Object> params) {
        String guildId = params.get("guild_id").toString();
        endGuildService.updateStatus(BotPlatform.QQ_GUILD, params.get("guild_id").toString(),
                EndGuildStatus.STATUS.NORMAL);
        log.info("bot at platform {} join guild {}", BotPlatform.QQ_GUILD.name(), guildId);
        Guild guild = bot.getGuild(Identifies.ID(guildId));
        if (guild != null) {
            Channel channel = guild.getChannels().limit(1).collectToList().get(0);
            channel.sendBlocking(toTextMessage(qgWelcome().displayWithFooter()));
        }
    }

    private QGContentTemplate qgWelcome() {
        QGContentTemplate ct = new QGContentTemplate("大家好，我是AXBot v2");
        ct.addLine("我是一个机器人，初次见面，请多多关照！");
        ct.addLine("你可以@我，并使用命令触发我的功能。如果你不知道怎么开始，请@我，并输入“帮助”");
        return ct;
    }
}
