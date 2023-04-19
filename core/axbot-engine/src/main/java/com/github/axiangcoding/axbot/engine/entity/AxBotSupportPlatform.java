package com.github.axiangcoding.axbot.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AxBotSupportPlatform {
    PLATFORM_KOOK("kook"),
    PLATFORM_CQHTTP("cqhttp");

    private final String name;
}
