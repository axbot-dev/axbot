package com.github.axiangcoding.axbot.app.server.data.entity.field;

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
public class EndUserUsage {
    Integer inputToday;
    Long inputTotal;
    Integer queryWtToday;
    Long queryWtTotal;

    public static EndUserUsage init() {
        return new EndUserUsage()
                .setInputToday(0)
                .setInputTotal(0L)
                .setQueryWtToday(0)
                .setQueryWtTotal(0L);
    }
}
