package com.github.axiangcoding.axbot.server.service.axbot.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;

@Getter
@Setter
@ToString
public abstract class AxBotOutput {
    String replayToUser;
    String content;
    String replayToMsg;

    Duration timeUsage;
}