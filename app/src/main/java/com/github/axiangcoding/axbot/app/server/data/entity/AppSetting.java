package com.github.axiangcoding.axbot.app.server.data.entity;

import com.github.axiangcoding.axbot.app.server.data.entity.basic.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 应用配置表
 */
@Getter
@Setter
@ToString
@Entity
public class AppSetting extends BasicEntity {
    @AllArgsConstructor
    @Getter
    public enum KEY {
        ;
        private final String label;
    }

    @Column(unique = true)
    String name;
    String val;
    String description;
}
