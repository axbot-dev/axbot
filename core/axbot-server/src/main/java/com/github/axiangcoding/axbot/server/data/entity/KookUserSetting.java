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

    public KookUserSetting() {
        super();
        this.usage = new Usage();
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
