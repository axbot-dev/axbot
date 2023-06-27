package com.github.axiangcoding.axbot.app.server.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
public class AppUser extends BasicEntity {
    public enum STATUS {
        NORMAL,
        LOCKED,
    }

    @Column(unique = true)
    String userId;
    @Column(unique = true)
    String username;
    String password;
    LocalDateTime lastLoginTime;

    Integer loginFailed;
    LocalDateTime lastLoginFailedTime;
    String status;
    Boolean isSuperAdmin;
}
