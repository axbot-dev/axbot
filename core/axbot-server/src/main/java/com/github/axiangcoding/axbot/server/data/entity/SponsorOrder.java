package com.github.axiangcoding.axbot.server.data.entity;

import com.github.axiangcoding.axbot.server.data.entity.basic.BasicEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class SponsorOrder extends BasicEntity {
    @AllArgsConstructor
    @Getter
    public enum PLAN {
        BASIC_PERSONAL("basic_personal");
        final String name;
    }

    @AllArgsConstructor
    @Getter
    public enum STATUS {
        PENDING("pending"),
        CLOSE("close"),
        SUCCESS("success");

        final String name;
    }

    UUID orderId;
    String plan;
    String planTitle;
    Integer month;
    String fromUserId;
    /**
     * 对kook是guild id，对qq是group id
     */
    String fromGuildId;
    String fromChannelId;
    String platform;
    String status;
}
