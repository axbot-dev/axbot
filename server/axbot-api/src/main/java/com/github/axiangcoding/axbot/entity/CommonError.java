package com.github.axiangcoding.axbot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonError {
    SUCCESS(0, "success"),
    ERROR(-1, "error"),
    INVALID_PARAM(10000, "invalid param"),
    RESOURCE_NOT_EXIST(10001, "resource not exist"),

    ;


    private final int code;
    private final String message;


}
