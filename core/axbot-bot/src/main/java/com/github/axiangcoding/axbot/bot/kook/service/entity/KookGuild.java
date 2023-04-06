package com.github.axiangcoding.axbot.bot.kook.service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class KookGuild {
    String id;
    String name;
    String topic;
    String masterId;
    String userId;
    Boolean isMaster;
    String icon;
    String notifyType;
    String region;
    String enableOpen;
    String openId;
    String defaultChannelId;
    String welcomeChannelId;
    String boostNum;
    String level;
    List<Role> roles;
    List<Channel> channels;

    @Getter
    @Setter
    public static class Role {
        Long roleId;
        String name;
        Long color;
        Integer position;
        Integer hoist;
        Integer mentionable;
        Integer permissions;
    }

    @Getter
    @Setter
    public static class Channel {
        String id;
        String guildId;
        String userId;
        String parentId;
        String name;
        String topic;
        Integer type;
        Integer level;
        Integer slowMode;
        Boolean isCategory;
    }
}
