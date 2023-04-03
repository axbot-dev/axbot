package com.github.axiangcoding.axbot.server.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
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

    public Map<String, Object> toDislayMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("userId", userId);
        map.put("username", username);
        map.put("createTime", createTime);
        map.put("lastLoginTime", lastLoginTime);
        return map;
    }
}
