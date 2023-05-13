package com.github.axiangcoding.axbot.server.data.entity;

import com.github.axiangcoding.axbot.server.data.entity.basic.BasicEntity;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserPermit;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserSubscribe;
import com.github.axiangcoding.axbot.server.data.entity.basic.UserUsage;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
public class KookUserSetting extends BasicEntity {

    @Column(unique = true)
    String userId;

    Boolean banned;
    String bannedReason;
    LocalDateTime bannedTime;
    @Embedded
    UserUsage usage;
    @Embedded
    UserPermit permit;
    @Embedded
    UserSubscribe subscribe;

    public static KookUserSetting defaultSetting(String userId) {
        KookUserSetting setting = new KookUserSetting();
        setting.setUserId(userId);
        setting.setBanned(false);
        setting.setUsage(UserUsage.defaultUsage());
        setting.setPermit(UserPermit.defaultPermit());
        setting.setSubscribe(new UserSubscribe());
        return setting;
    }


}
