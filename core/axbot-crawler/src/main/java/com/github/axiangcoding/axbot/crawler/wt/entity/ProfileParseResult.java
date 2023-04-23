package com.github.axiangcoding.axbot.crawler.wt.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileParseResult {
    Boolean found;
    GamerProfile profile;

    @Data
    public static class GamerProfile {
        String nickname;

        String clan;
        String clanUrl;
        Boolean banned;
        LocalDate registerDate;
        String title;
        Integer level;
        UserStat statAb;
        UserStat statRb;
        UserStat statSb;

        GroundRate groundRateAb;
        GroundRate groundRateRb;
        GroundRate groundRateSb;


        AviationRate aviationRateAb;
        AviationRate aviationRateRb;
        AviationRate aviationRateSb;


        FleetRate fleetRateAb;
        FleetRate fleetRateRb;
        FleetRate fleetRateSb;

        // TS街机效率值
        Double TsABRate;
        // TS历史效率值
        Double TsRBRate;
        // TS全真效率值
        Double TsSBRate;


        @Data
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

        @Data
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

        @Data
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

        @Data
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
    }
}
