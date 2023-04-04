package com.github.axiangcoding.axbot.engine.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;

@Getter
@Setter
@ToString
public abstract class AxBotUserOutput {
    String replayToUser;
    String content;
    String replayToMsg;

    Duration timeUsage;
}
