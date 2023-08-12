package com.github.axiangcoding.axbot.app.third.pubg.service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PubgRespData<T> extends PubgBaseResponse {
    T data;
}
