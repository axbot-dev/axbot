package com.github.axiangcoding.axbot.app.server.data.entity;

import com.github.axiangcoding.axbot.app.server.data.entity.basic.BasicEntity;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.pubg.PubgLifetimeStats;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.pubg.PubgPlayerInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
public class PubgPlayer extends BasicEntity {
    @Column(unique = true)
    String playerId;

    String playerName;

    @Embedded
    PubgPlayerInfo info;

    @Embedded
    PubgLifetimeStats lifetimeStats;
}
