package com.github.axiangcoding.axbot.server.data.entity;

import com.github.axiangcoding.axbot.server.data.entity.basic.*;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
    @Embedded
    UserUsage usage;
    @Embedded
    UserSubscribe subscribe;
    @Embedded
    UserPermit permit;
    @Embedded
    BindProfile bindProfile;

    public static QUserSetting defaultSetting(String userId) {
        QUserSetting setting = new QUserSetting();
        setting.setUserId(userId);
        setting.setBanned(false);
        setting.setUsage(UserUsage.defaultUsage());
        setting.setSubscribe(new UserSubscribe());
        setting.setPermit(UserPermit.defaultPermit());
        return setting;
    }
}
