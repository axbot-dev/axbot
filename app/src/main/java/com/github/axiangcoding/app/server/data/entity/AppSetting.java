package com.github.axiangcoding.app.server.data.entity;

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


    @Column(unique = true, nullable = false)
    String name;
    String val;
    String description;
}
