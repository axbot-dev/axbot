package com.github.axiangcoding.axbot.engine.v1;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum NotificationEvent {
    EVENT_JOIN_GUILD(),
    EVENT_EXIT_GUILD(),
    EVENT_BILI_ROOM_REMIND(),
    EVENT_WT_NEWS(),
}
