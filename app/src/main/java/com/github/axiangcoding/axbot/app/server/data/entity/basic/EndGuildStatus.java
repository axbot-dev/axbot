package com.github.axiangcoding.axbot.app.server.data.entity.basic;

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
public class EndGuildStatus {
    public enum STATUS {
        NORMAL,
        LOCKED,
    }

    String status;
    String bannedReason;
    LocalDateTime bannedAt;
    LocalDateTime bannedUntil;

    public static EndGuildStatus normal() {
        return new EndGuildStatus().setStatus(STATUS.NORMAL.name());
    }
}
