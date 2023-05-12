package com.github.axiangcoding.axbot.engine.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum SupportPlatform {
    PLATFORM_KOOK("kook"),
    PLATFORM_CQHTTP("cqhttp");

    private final String name;

    public static SupportPlatform getByName(String name) {
        for (SupportPlatform value : SupportPlatform.values()) {
            if (StringUtils.equals(name, value.getName())) {
                return value;
            }
        }
        return null;
    }
}
