package com.github.axiangcoding.axbot.bot.kook.service.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GuildViewResp extends CommonResp {
    DataItem data;

    @Getter
    @Setter
    public static class DataItem {
        String id;
        String name;
        String topic;
        String userId;
        String icon;
        Integer notifyType;
        String region;
        Boolean enableOpen;
        String openId;
        String defaultChannelId;
        String welcomeChannelId;
        List<Role> roles;
        List<Channel> channels;
    }

    @Getter
    @Setter
    public static class Role {
        Integer roleId;
        String name;
        Integer color;
        Integer position;
        Integer hoist;
        Integer mentionable;
        Integer permissions;
    }

    @Getter
    @Setter
    public static class Channel {
        Integer id;
        String guildId;
        String userId;
        Integer parentId;
        String name;
        String topic;
        Integer type;
        Integer level;
        Integer slowMode;
        Boolean isCategory;
    }
}
