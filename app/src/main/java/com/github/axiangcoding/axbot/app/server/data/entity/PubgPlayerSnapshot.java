package com.github.axiangcoding.axbot.app.server.data.entity;

import com.github.axiangcoding.axbot.app.server.data.entity.basic.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
public class PubgPlayerSnapshot extends BasicEntity {
    String playerId;

    @Column(columnDefinition = "json")
    String info;

    @Column(columnDefinition = "json")
    String lifetimeStats;
}
