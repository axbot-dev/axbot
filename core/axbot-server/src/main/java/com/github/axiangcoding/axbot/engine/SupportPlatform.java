package com.github.axiangcoding.axbot.engine;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SupportPlatform {
    KOOK("kook"),
    CQHTTP("cqhttp");

    private final String label;

}
