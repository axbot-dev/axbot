package com.github.axiangcoding.axbot.engine.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public abstract class AxBotInput {
    String requestUser;
    String requestMsgId;
    String requestCommand;
    LocalDateTime startTime;

    public AxBotInput() {
        startTime = LocalDateTime.now();
    }
}
