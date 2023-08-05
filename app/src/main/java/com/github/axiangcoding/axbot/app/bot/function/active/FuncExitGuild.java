package com.github.axiangcoding.axbot.app.bot.function.active;

import com.github.axiangcoding.axbot.app.bot.annotation.AxActiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.ActiveEvent;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.function.AbstractActiveFunction;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.EndGuildStatus;
import com.github.axiangcoding.axbot.app.server.service.EndGuildService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.bot.Bot;

import java.util.Map;

@AxActiveFunc(event = ActiveEvent.EXIT_GUILD)
@Slf4j
public class FuncExitGuild extends AbstractActiveFunction {
    @Resource
    EndGuildService endGuildService;

    @Override
    public void processByKOOK(Bot bot, Map<String, Object> params) {
        String guildId = params.get("guild_id").toString();
        endGuildService.updateStatus(BotPlatform.KOOK, guildId,
                EndGuildStatus.STATUS.DISABLED);
        log.info("bot at platform {} exit guild {}", BotPlatform.KOOK.name(), guildId);
    }

    @Override
    public void processByQG(Bot bot, Map<String, Object> params) {
        String guildId = params.get("guild_id").toString();
        endGuildService.updateStatus(BotPlatform.QQ_GUILD, params.get("guild_id").toString(),
                EndGuildStatus.STATUS.DISABLED);
        log.info("bot at platform {} exit guild {}", BotPlatform.QQ_GUILD.name(), guildId);
    }
}
