package com.github.axiangcoding.axbot.server.service.axbot.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public abstract class AxBotInput {
    String fromUserId;
    String fromMsgId;
    String requestCommand;
    LocalDateTime startTime;

    public AxBotInput() {
        startTime = LocalDateTime.now();
    }
}
