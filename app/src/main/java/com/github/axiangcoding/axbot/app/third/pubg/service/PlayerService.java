package com.github.axiangcoding.axbot.app.third.pubg.service;

import com.github.axiangcoding.axbot.app.third.pubg.service.entity.PubgRespData;
import com.github.axiangcoding.axbot.app.third.pubg.service.entity.PubgRespDataList;
import com.github.axiangcoding.axbot.app.third.pubg.service.entity.resp.Player;
import com.github.axiangcoding.axbot.app.third.pubg.service.entity.resp.PlayerSeason;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlayerService {

    @GET("/shards/{platform}/players")
    Single<PubgRespDataList<Player>> getPlayers(
            @Path("platform") String platform,
            @Query("filter[playerNames]") String playerNames,
            @Query("filter[playerIds]") String playerIds);

    @GET("/shards/{platform}/players/{accountId}/seasons/lifetime")
    Single<PubgRespData<PlayerSeason>> getPlayerLifetime(
            @Path("platform") String platform,
            @Path("accountId") String accountId,
            @Query("filter[gamepad]") Boolean gamepad);
}
