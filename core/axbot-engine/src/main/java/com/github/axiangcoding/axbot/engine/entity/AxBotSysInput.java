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
    Map<String, Object> extraMap;

}
