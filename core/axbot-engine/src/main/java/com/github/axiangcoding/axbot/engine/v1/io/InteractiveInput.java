package com.github.axiangcoding.axbot.engine.v1.io;

import com.github.axiangcoding.axbot.engine.v1.InteractiveCommand;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 交互类输入
 */
@Getter
@Setter
@ToString
public abstract class InteractiveInput {
    UUID interactiveId;
    String userId;
    String messageId;
    InteractiveCommand command;
    String[] paramList;
    LocalDateTime startTime;

    public InteractiveInput() {
        interactiveId = UUID.randomUUID();
        startTime = LocalDateTime.now();
    }

    public abstract InteractiveOutput response(String response);
}
