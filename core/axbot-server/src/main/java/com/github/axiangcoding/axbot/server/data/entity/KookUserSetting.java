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
    public static final Integer INPUT_LIMIT = 50;

    @Column(unique = true)
    String userId;

    Boolean banned;
    String bannedReason;
    LocalDateTime bannedTime;
    @Embedded
    Usage usage;
    @Embedded
    Permit permit;
    @Embedded
    Subscribe subscribe;

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
        Permit permit = new Permit();
        permit.setCanUseAI(false);
        setting.setPermit(permit);
        setting.setSubscribe(new Subscribe());
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

    @Embeddable
    @Getter
    @Setter
    @ToString
    public static class Permit {
        Boolean canUseAI;
    }

    @Embeddable
    @Getter
    @Setter
    @ToString
    public static class Subscribe {
        String plan;
        LocalDateTime expireAt;
    }
}
