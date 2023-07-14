package com.github.axiangcoding.axbot.app.server.data.entity.basic;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Embeddable
@Accessors(chain = true)
public class EndGuildSetting {
    Boolean enabledWtProfileQuery;

    /**
     * 启用战雷新闻提醒
     */
    Boolean enableWtNewsReminder;

    /**
     * 发布战雷新闻的频道id
     */
    String wtNewsChannelId;

    /**
     * 发布新闻@的角色id
     */
    String wtNewsAtRoleId;

    /**
     * 启用bilibili直播提醒
     */
    Boolean enableBiliLiveReminder;

    /**
     * bilibili直播提醒的频道id
     */
    String biliLiveChannelId;

    /**
     * 配置的bilibili直播间id
     */
    String biliRoomId;
}
