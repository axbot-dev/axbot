package com.github.axiangcoding.axbot.server.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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
    Usage usage;

    public static KookUserSetting defaultSetting(String userId) {
        KookUserSetting setting = new KookUserSetting();
        setting.setUserId(userId);
        setting.setBanned(false);
        Usage usage = new Usage();
        usage.setInputTotal(0L);
        usage.setQueryWtTotal(0L);
        usage.setInputToday(0);
        usage.setQueryWtToday(0);
        setting.setUsage(usage);
        return setting;
    }

    @Embeddable
    @Getter
    @Setter
    @ToString
    public static class Usage {
        Integer inputToday;
        Long inputTotal;
        Integer queryWtToday;
        Long queryWtTotal;
    }
}
