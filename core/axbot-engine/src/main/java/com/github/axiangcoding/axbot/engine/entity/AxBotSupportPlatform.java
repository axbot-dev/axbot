package com.github.axiangcoding.axbot.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum AxBotSupportPlatform {
    PLATFORM_KOOK("kook"),
    PLATFORM_CQHTTP("cqhttp");

    private final String name;

    public static AxBotSupportPlatform getByName(String name) {
        for (AxBotSupportPlatform value : AxBotSupportPlatform.values()) {
            if (StringUtils.equals(name, value.getName())) {
                return value;
            }
        }
        return null;
    }
}
