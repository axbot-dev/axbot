package com.github.axiangcoding.axbot.server.data.entity;

import com.github.axiangcoding.axbot.server.data.entity.basic.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(indexes = {
        @Index(columnList = "bindProfile_wtNickname"),
})
public class KookUserSetting extends BasicEntity {
    @AllArgsConstructor
    @Getter
    public enum ROLE {
        SUPER_ADMIN("superAdmin"),
        ADMIN("admin"),
        NORMAL("normal");

        private final String label;
    }


    @Column(unique = true)
    String userId;

    Boolean banned;
    String bannedReason;
    LocalDateTime bannedTime;

    String role;
    @Embedded
    BindProfile bindProfile;
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
        setting.setBindProfile(new BindProfile());
        return setting;
    }


}
