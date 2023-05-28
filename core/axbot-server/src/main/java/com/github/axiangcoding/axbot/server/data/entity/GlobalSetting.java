package com.github.axiangcoding.axbot.server.data.entity;

import com.github.axiangcoding.axbot.server.data.entity.basic.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class GlobalSetting extends BasicEntity {
    @AllArgsConstructor
    @Getter
    public enum KEY {
        WT_PROFILE_CRAWLER_MODE("WTProfileCrawlerMode"),
        DB_UPDATE_VERSION("DBUpdateVersion");

        private final String label;
    }


    @Column(unique = true)
    String key;

    @Column(columnDefinition = "TEXT")
    String value;

    String remark;
}
