package com.github.axiangcoding.app.server.data.entity;

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
    @Column(unique = true)
    String userId;
    @Column(unique = true)
    String username;
    String password;
    LocalDateTime lastLoginTime;
    Boolean isSuperAdmin;
}
