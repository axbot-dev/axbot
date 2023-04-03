package com.github.axiangcoding.axbot.server.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class GlobalSetting extends BasicEntity {
    public static final String KEY_WT_PROFILE_CRAWLER_MODE = "WTProfileCrawlerMode";
    public static final String KEY_DB_UPDATE_VERSION = "DBUpdateVersion";

    @Column(unique = true)
    String key;

    @Column(columnDefinition = "TEXT")
    String value;

    String remark;
}
