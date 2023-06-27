package com.github.axiangcoding.axbot.app.server.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(indexes = {@Index(columnList = "userId")})
@Accessors(chain = true)
public class ApiKey extends BasicEntity {
    @Column(unique = true)
    String val;
    String userId;
    String description;
    Boolean neverExpire;
    LocalDateTime expireTime;
}
