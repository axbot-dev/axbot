package com.github.axiangcoding.axbot.server.data.entity;

import com.github.axiangcoding.axbot.server.data.entity.basic.BasicEntity;
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
public class QGroupSetting extends BasicEntity {
    @Column(unique = true)
    String groupId;

    Boolean active;
    Boolean banned;

    @Embedded
    FunctionSetting functionSetting;

    public static QGroupSetting defaultSetting(String groupId) {
        QGroupSetting setting = new QGroupSetting();
        setting.setGroupId(groupId);
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
         * 启用bilibili直播提醒
         */
        Boolean enableBiliLiveReminder;

        /**
         * 配置的bilibili直播间id
         */
        String biliRoomId;
    }
}
