package com.github.axiangcoding.axbot.app.third.bilibili.service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BiliResponse<T> {
    Integer code;
    String message;
    String msg;
    T data;
}
