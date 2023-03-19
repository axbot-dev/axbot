package com.github.axiangcoding.axbot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class GlobalSetting extends BasicEntity {
    public static final String KEY_WT_PROFILE_CRAWLER_MODE = "WTProfileCrawlerMode";

    @Column(unique = true)
    String key;

    @Column(columnDefinition = "TEXT")
    String value;

    String remark;
}
