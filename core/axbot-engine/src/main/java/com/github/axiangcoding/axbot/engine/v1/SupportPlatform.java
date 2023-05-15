package com.github.axiangcoding.axbot.engine.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SupportPlatform {
    PLATFORM_KOOK("kook"),
    PLATFORM_CQHTTP("cqhttp");

    private final String label;

}
