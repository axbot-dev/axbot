package com.github.axiangcoding.axbot.engine.v1.io;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;
import java.util.UUID;

/**
 * 通知类输出
 */
@Getter
@Setter
@ToString
public class NotificationOutput {
    UUID notificationId;
    Duration timeUsage;
    String userId;
    String messageId;
    String response;
}
