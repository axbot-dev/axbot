package com.github.axiangcoding.axbot.app.third.pubg.service.entity.resp;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlayerSeason {
    String type;
    Attributes attributes;
    Relationships relationships;

    @Getter
    @Setter
    @ToString
    public static class Attributes {
        Double bestRankPoint;
        GameModeStats gameModeStats;


        @Getter
        @Setter
        @ToString
        public static class GameModeStats {
            GameStats duo;
            @SerializedName("duo-fpp")
            GameStats duoFPP;
            GameStats solo;
            @SerializedName("solo-fpp")
            GameStats soloFPP;
            GameStats squad;
            @SerializedName("squad-fpp")
            GameStats squadFPP;
        }

    }

    @Getter
    @Setter
    @ToString
    public static class Relationships {
        Object player;
        Object matchesSolo;
        Object matchesSoloFPP;
        Object matchesDuo;
        Object matchesDuoFPP;
        Object matchesSquad;
        Object matchesSquadFPP;
        Object season;
    }
}
