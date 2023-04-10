package com.github.axiangcoding.axbot.remote.kook.service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KookResponse<T> {
    Integer code;
    String message;
    T data;
}
