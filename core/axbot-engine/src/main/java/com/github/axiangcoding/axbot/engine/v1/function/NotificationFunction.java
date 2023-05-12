package com.github.axiangcoding.axbot.engine.v1.function;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class NotificationFunction {
    final String name;
    final String description;

    public NotificationFunction(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract String execute();

}
