package com.github.axiangcoding.axbot.app.server.data.entity;

import com.github.axiangcoding.axbot.app.server.data.entity.basic.BasicEntity;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.PubgPlayerInfo;
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
public class PubgPlayerSnapshot extends BasicEntity {
    String playerId;

    @Embedded
    PubgPlayerInfo info;
}
