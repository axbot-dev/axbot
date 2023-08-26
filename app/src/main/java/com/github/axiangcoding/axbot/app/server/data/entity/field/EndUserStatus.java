package com.github.axiangcoding.axbot.app.server.data.entity.field;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Embeddable
@Accessors(chain = true)
public class EndUserStatus {
    public enum STATUS {
        NORMAL,
        LOCKED,
    }

    String status;
    String bannedReason;
    LocalDateTime bannedAt;
    LocalDateTime bannedUntil;

    public static EndUserStatus normal() {
        return new EndUserStatus().setStatus(EndGuildStatus.STATUS.NORMAL.name());
    }
}
