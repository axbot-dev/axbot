package com.github.axiangcoding.axbot.remote.cqhttp.service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CqhttpResponse<T> {
    String status;
    Integer retcode;
    String msg;
    String wording;
    String echo;
    T data;
}
