package com.github.axiangcoding.app.bot.listener;

import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.ChannelMessageEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyListener {
    @Listener
    @Filter(targets = @Filter.Targets(atBot = true))
    public void onEvent(ChannelMessageEvent event) {
        // Items<GuildMember> members = event.getSource().getGuild().getMembers();
        // List<GuildMember> guildMembers = members.collectToList();
        // guildMembers.forEach(
        //         member -> log.info("成员: {} - {}", member.getNickname(), member.getId())
        // );
        log.info("监听到了 {}", event.getMessageContent().getPlainText());
        event.replyAsync("你好，我是机器人");
        event.replyAsync("再发一条");
    }
}
