package com.github.axiangcoding.axbot.remote.afdian.service.entity;

import lombok.Data;

@Data
public class AfdianResponse<T> {
    Integer ec;
    String em;
    T data;
}
