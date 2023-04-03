package com.github.axiangcoding.axbot.server.data.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
public class ApiKey extends BasicEntity {
    String name;
    UUID creator;
    String content;
    String comment;
    Boolean neverExpire;
    LocalDateTime expireTime;
}
