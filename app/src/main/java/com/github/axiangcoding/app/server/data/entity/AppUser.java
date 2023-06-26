package com.github.axiangcoding.app.server.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser extends BasicEntity {
    @Column(unique = true)
    String userId;
    @Column(unique = true)
    String username;
    String password;
    LocalDateTime lastLoginTime;
    Boolean isSuperAdmin;
}
