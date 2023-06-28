package com.github.axiangcoding.axbot.app.server.data.entity.basic;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 订阅信息
 */
@Getter
@Setter
@ToString
@Embeddable
@Accessors(chain = true)
public class SponsorPlan {
    public enum TYPE {
        GOLD,
        SILVER,
        BRONZE,
    }

    String type;
    LocalDateTime expireAt;
}
