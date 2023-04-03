package com.github.axiangcoding.axbot.server.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class KookGuildSetting extends BasicEntity {
    @Column(unique = true)
    String guildId;

    Integer WTProfileUpdateUsage;
    Integer WTProfileUpdateLimit;
    Boolean banned;
    Boolean active;

}
