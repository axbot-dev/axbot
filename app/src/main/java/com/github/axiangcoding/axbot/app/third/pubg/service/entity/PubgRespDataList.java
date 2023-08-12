package com.github.axiangcoding.axbot.app.third.pubg.service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PubgRespDataList<T> extends PubgBaseResponse {
    List<T> data;
}

