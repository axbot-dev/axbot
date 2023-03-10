package com.github.axiangcoding.axbot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonError {
    SUCCESS(0, "success"),
    ERROR(-1, "error");

    private final int code;
    private final String message;


}
