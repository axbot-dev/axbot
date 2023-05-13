package com.github.axiangcoding.axbot.engine.v1.io;

import com.github.axiangcoding.axbot.engine.v1.InteractiveCommand;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 交互类输入
 */
@Getter
@Setter
@ToString
public abstract class InteractiveInput {
    Long inputId;
    String userId;
    String messageId;
    InteractiveCommand command;
    String[] paramList;
    LocalDateTime startTime;

    public InteractiveInput() {
        startTime = LocalDateTime.now();
    }

    public abstract InteractiveOutput response(String response);
}
