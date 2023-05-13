package com.github.axiangcoding.axbot.server.data.entity;

import com.github.axiangcoding.axbot.server.data.entity.basic.BasicEntity;
import jakarta.persistence.Column;
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
public class GlobalUser extends BasicEntity {
    @Column(unique = true)
    UUID userId;
    @Column(unique = true)
    String username;
    String password;
    LocalDateTime lastLoginTime;
    Boolean isAdmin;
}
