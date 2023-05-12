package com.github.axiangcoding.axbot.engine.v1.io;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 通知类输入
 */
@Getter
@Setter
@ToString
public abstract class NotificationInput {
    UUID notificationId;
    LocalDateTime startTime;

    public NotificationInput() {
        notificationId = UUID.randomUUID();
        startTime = LocalDateTime.now();
    }

    public abstract NotificationOutput response();
}
