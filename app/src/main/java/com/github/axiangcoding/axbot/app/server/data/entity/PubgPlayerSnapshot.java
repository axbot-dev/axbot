package com.github.axiangcoding.axbot.app.server.data.entity;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.BasicEntity;
import com.github.axiangcoding.axbot.app.server.data.entity.field.PubgLifetimeStats;
import com.github.axiangcoding.axbot.app.server.data.entity.field.PubgPlayerInfo;
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

    public PubgPlayerInfo getDecodeInfo() {
        return info == null ? null : JSONObject.parseObject(info, PubgPlayerInfo.class);
    }

    public PubgPlayerSnapshot setEncodeInfo(PubgPlayerInfo info) {
        this.info = JSONObject.toJSONString(info);
        return this;
    }

    public PubgLifetimeStats getDecodeLifetimeStats() {
        return lifetimeStats == null ? null : JSONObject.parseObject(lifetimeStats, PubgLifetimeStats.class);
    }

    public PubgPlayerSnapshot setEncodeLifetimeStats(PubgLifetimeStats lifetimeStats) {
        this.lifetimeStats = JSONObject.toJSONString(lifetimeStats);
        return this;
    }
}
