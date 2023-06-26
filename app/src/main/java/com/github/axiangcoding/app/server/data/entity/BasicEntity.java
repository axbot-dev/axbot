package com.github.axiangcoding.app.server.data.entity;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 基础表结构
 */
@MappedSuperclass
@Getter
@Setter
public class BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDateTime createTime;
    LocalDateTime updateTime;


    public BasicEntity() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
}
