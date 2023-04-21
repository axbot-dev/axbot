package com.github.axiangcoding.axbot.remote.qiniu.service.entity;

import lombok.Data;

@Data
public class QiniuResponse<T> {
    Integer code;
    String message;
    T result;
}
