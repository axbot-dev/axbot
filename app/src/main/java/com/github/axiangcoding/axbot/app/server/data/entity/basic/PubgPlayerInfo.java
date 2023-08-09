package com.github.axiangcoding.axbot.app.server.data.entity.basic;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Embeddable
@Accessors(chain = true)
public class PubgPlayerInfo {
    String titleId;
    String shardId;
    String patchVersion;
    String banType;
    String clanId;
    String name;
    // Object stats;
}
