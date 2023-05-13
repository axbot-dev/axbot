package com.github.axiangcoding.axbot.engine.v1.io;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;

/**
 * 交互类输出
 */
@Getter
@Setter
@ToString
public abstract class InteractiveOutput {
    Long inputId;
    String userId;
    String messageId;
    String response;
    Duration timeUsage;
}
