package com.github.axiangcoding.axbot.app.third.qiniu.service.entity;

import lombok.Data;

@Data
public class QiniuResponse<T> {
    Integer code;
    String message;
    T result;
}
