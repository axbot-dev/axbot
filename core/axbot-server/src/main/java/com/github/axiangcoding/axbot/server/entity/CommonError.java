package com.github.axiangcoding.axbot.server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonError {
    SUCCESS(0, "success"),
    ERROR(-1, "error"),
    INVALID_PARAM(10000, "invalid param"),
    RESOURCE_NOT_EXIST(10001, "resource not exist"),
    WT_GAMER_PROFILE_REFRESH_TOO_OFTEN(30000, "WT gamer profile refresh too often")
    ;


    private final int code;
    private final String message;


}
