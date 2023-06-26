package com.github.axiangcoding.app.server.controller.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonError {
    SUCCESS(0, "success"),
    ERROR(-1, "error"),
    INVALID_PARAM(10000, "invalid param"),
    RESOURCE_NOT_EXIST(10001, "resource not exist"),
    NOT_AUTHORIZED(10002, "not authorized"),
    NOT_PERMIT(10003, "not permit"),
    NOT_SUPPORT(10004, "not support yet."),
    API_KEY_INVALID(10005,"api key not exist or expired"),
    LOGIN_FAILED(20000, "login failed"),
    REGISTER_FAILED(20001, "register failed"),
    UPDATE_PASSWORD_FAILED(20002, "update password failed"),;


    private final int code;
    private final String message;

}
