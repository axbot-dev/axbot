package com.github.axiangcoding.axbot.engine.v1;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum NotificationEvent {
    JOIN_GUILD(),
    EXIT_GUILD(),
    BILI_ROOM_REMIND(),
    WT_NEWS(),
}
