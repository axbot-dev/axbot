package com.github.axiangcoding.axbot.server.data.entity;


import com.github.axiangcoding.axbot.crawler.entity.ParserResult;
import com.github.axiangcoding.axbot.server.util.JsonUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table
public class WtGamerProfile extends BasicEntity {

    @Column(unique = true)
    String nickname;

    String clan;
    String clanUrl;
    Boolean banned;
    LocalDate registerDate;
    String title;
    Integer level;

    @Embedded
    UserStat statAb;
    @Embedded
    UserStat statRb;
    @Embedded
    UserStat statSb;

    @Embedded
    GroundRate groundRateAb;
    @Embedded
    GroundRate groundRateRb;
    @Embedded
    GroundRate groundRateSb;

    @Embedded
    AviationRate aviationRateAb;
    @Embedded
    AviationRate aviationRateRb;
    @Embedded
    AviationRate aviationRateSb;

    @Embedded
    FleetRate fleetRateAb;
    @Embedded
    FleetRate fleetRateRb;
    @Embedded
    FleetRate fleetRateSb;

    // TS街机效率值
    Double TsABRate;
    // TS历史效率值
    Double TsRBRate;
    // TS全真效率值
    Double TsSBRate;


    @Getter
    @Setter
    @Embeddable
    public static class UserStat {
        Integer totalMission;
        Double winRate;
        Integer groundDestroyCount;
        Integer fleetDestroyCount;
        String gameTime;
        Integer aviationDestroyCount;
        Integer winCount;
        Long sliverEagleEarned;
        Integer deadCount;
    }

    @Getter
    @Setter
    @Embeddable
    public static class GroundRate {
        Integer gameCount;
        Integer groundVehicleGameCount;
        Integer tdGameCount;
        Integer htGameCount;
        Integer spaaGameCount;
        String gameTime;
        String groundVehicleGameTime;
        String tdGameTime;
        String htGameTime;
        String spaaGameTime;
        Integer totalDestroyCount;
        Integer aviationDestroyCount;
        Integer groundDestroyCount;
        Integer fleetDestroyCount;
    }

    @Getter
    @Setter
    @Embeddable
    public static class AviationRate {
        Integer gameCount;
        Integer fighterGameCount;
        Integer bomberGameCount;
        Integer attackerGameCount;
        String gameTime;
        String fighterGameTime;
        String bomberGameTime;
        String attackerGameTime;
        Integer totalDestroyCount;
        Integer aviationDestroyCount;
        Integer groundDestroyCount;
        Integer fleetDestroyCount;
    }

    @Getter
    @Setter
    @Embeddable
    public static class FleetRate {
        Integer gameCount;
        Integer fleetGameCount;
        Integer torpedoBoatGameCount;
        Integer gunboatGameCount;
        Integer torpedoGunboatGameCount;
        Integer submarineHuntGameCount;
        Integer destroyerGameCount;
        Integer navyBargeGameCount;
        String gameTime;
        String fleetGameTime;
        String torpedoBoatGameTime;
        String gunboatGameTime;
        String torpedoGunboatGameTime;
        String submarineHuntGameTime;
        String destroyerGameTime;
        String navyBargeGameTime;
        Integer totalDestroyCount;
        Integer aviationDestroyCount;
        Integer groundDestroyCount;
        Integer fleetDestroyCount;
    }

    public static WtGamerProfile from(ParserResult.GamerProfile gp) {
        return JsonUtils.fromJson(JsonUtils.toJson(gp), WtGamerProfile.class);
    }
}
