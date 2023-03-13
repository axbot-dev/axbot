package com.github.axiangcoding.axbot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonError {
    SUCCESS(0, "success"),
    INVALID_PARAM(10000, "invalid param"),
    ERROR(-1, "error");

    private final int code;
    private final String message;


}
