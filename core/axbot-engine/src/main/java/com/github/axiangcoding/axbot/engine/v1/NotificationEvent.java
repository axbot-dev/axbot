package com.github.axiangcoding.axbot.engine.v1;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum NotificationEvent {
    SYSTEM_EVENT_JOIN_GUILD(),
    SYSTEM_EVENT_EXIT_GUILD(),
    SYSTEM_EVENT_BILI_ROOM_REMIND(),
    SYSTEM_EVENT_WT_NEWS(),
}
