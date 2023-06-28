package com.github.axiangcoding.axbot.app.server.data.entity.basic;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 使用情况
 */
@Getter
@Setter
@ToString
@Embeddable
@Accessors(chain = true)
public class EndGuildUsage {
    Integer inputToday;
    Long inputTotal;
    Integer queryWtToday;
    Long queryWtTotal;

    public static EndGuildUsage init() {
        return new EndGuildUsage()
                .setInputToday(0)
                .setInputTotal(0L)
                .setQueryWtToday(0)
                .setQueryWtTotal(0L);
    }
}
