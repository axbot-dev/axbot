package com.github.axiangcoding.axbot.engine.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class AxBotSysInput {
    AxBotSystemEvent event;
    /**
     * 类型转换太拉跨，更换为extraJson为佳
     */
    @Deprecated
    Map<String, Object> extraMap;
    String extraJson;
}
