package com.github.axiangcoding.axbot.remote.kook.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum KookPermission {
    ADMIN(0x1L),
    ADMINISTRATION_SERVER(0x10L),
    VIEW_ADMIN_LOG(0x100L),
    CREATE_INVITE_LINK(0x1000L);

    final Long bit;

    public static boolean hasPermission(Long bits, KookPermission permission) {
        return (bits & permission.bit) == permission.bit;
    }
}
