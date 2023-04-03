package com.github.axiangcoding.axbot.server.service.axbot.entity;

import com.github.axiangcoding.axbot.server.service.axbot.handler.AxBotSystemEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AxBotSysInput {
    AxBotSystemEvent event;


}
