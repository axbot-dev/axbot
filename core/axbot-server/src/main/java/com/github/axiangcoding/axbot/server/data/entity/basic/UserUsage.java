package com.github.axiangcoding.axbot.server.data.entity.basic;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Embeddable
public class UserUsage {
    Integer inputToday;
    Long inputTotal;
    Integer queryWtToday;
    Long queryWtTotal;

    public static UserUsage defaultUsage() {
        UserUsage usage = new UserUsage();
        usage.setInputToday(0);
        usage.setInputTotal(0L);
        usage.setQueryWtToday(0);
        usage.setQueryWtTotal(0L);
        return usage;
    }
}
