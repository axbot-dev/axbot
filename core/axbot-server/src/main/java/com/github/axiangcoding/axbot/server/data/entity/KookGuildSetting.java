package com.github.axiangcoding.axbot.server.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class KookGuildSetting extends BasicEntity {
    @Column(unique = true)
    String guildId;

    Boolean banned;
    Boolean active;

    @Embedded
    FunctionSetting functionSetting;

    public static KookGuildSetting defaultSetting(String guildId) {
        KookGuildSetting setting = new KookGuildSetting();
        setting.setGuildId(guildId);
        setting.setBanned(false);
        setting.setActive(true);
        FunctionSetting fs = new FunctionSetting();
        fs.setEnableBiliLiveReminder(false);
        fs.setEnableWtNewsReminder(false);
        fs.setEnabledWtProfileQuery(true);
        setting.setFunctionSetting(fs);
        return setting;
    }

    @Getter
    @Setter
    @ToString
    @Embeddable
    public static class FunctionSetting {
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


}
