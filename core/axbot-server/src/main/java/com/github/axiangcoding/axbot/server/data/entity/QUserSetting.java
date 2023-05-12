package com.github.axiangcoding.axbot.server.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
public class QUserSetting extends BasicEntity {
    @Column(unique = true)
    String userId;

    Boolean banned;
    String bannedReason;
    LocalDateTime bannedTime;

    public static QUserSetting defaultSetting(String userId) {
        QUserSetting setting = new QUserSetting();
        setting.setUserId(userId);
        setting.setBanned(false);
        return setting;
    }
}
