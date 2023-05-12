package com.github.axiangcoding.axbot.server.data.entity;

import jakarta.persistence.Column;
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

    Boolean banned;

    public static QGroupSetting defaultSetting(String groupId) {
        QGroupSetting setting = new QGroupSetting();
        setting.setGroupId(groupId);
        setting.setBanned(false);
        return setting;
    }
}
