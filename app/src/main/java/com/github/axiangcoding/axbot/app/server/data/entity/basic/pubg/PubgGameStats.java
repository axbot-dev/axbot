package com.github.axiangcoding.axbot.app.server.data.entity.basic.pubg;

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
public class PubgGameStats {
    Integer assists;
    Integer boosts;
    Integer dBNOs;
    Integer dailyKills;
    Integer dailyWins;
    Double damageDealt;
    Integer days;
    Integer headshotKills;
    Integer heals;
    Integer killPoints;
    Integer kills;
    Double longestKill;
    Double longestTimeSurvived;
    Integer losses;
    Integer maxKillStreaks;
    Double mostSurvivalTime;
    Integer rankPoints;
    String rankPointsTitle;
    Integer revives;
    Double rideDistance;
    Integer roadKills;
    Integer roundMostKills;
    Integer roundsPlayed;
    Integer suicides;
    Double swimDistance;
    Integer teamKills;
    Double timeSurvived;
    Integer top10s;
    Integer vehicleDestroys;
    Double walkDistance;
    Integer weaponsAcquired;
    Integer weeklyKills;
    Integer weeklyWins;
    Integer winPoints;
    Integer wins;
}
