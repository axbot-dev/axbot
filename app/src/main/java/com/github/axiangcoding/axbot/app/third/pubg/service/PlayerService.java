package com.github.axiangcoding.axbot.app.third.pubg.service;

import com.github.axiangcoding.axbot.app.third.pubg.service.entity.PubgResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlayerService {

    @GET("/shards/{platform}/players")
    Single<PubgResponse<Object>> getPlayers(
            @Path("platform") String platform,
            @Query("filter[playerNames]") String playerNames,
            @Query("filter[playerIds]") String playerIds);

}
