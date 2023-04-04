package com.github.axiangcoding.axbot.engine.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public abstract class AxBotUserInput {
    String fromUserId;
    String fromMsgId;
    String requestCommand;
    LocalDateTime startTime;

    public AxBotUserInput() {
        startTime = LocalDateTime.now();
    }
}
