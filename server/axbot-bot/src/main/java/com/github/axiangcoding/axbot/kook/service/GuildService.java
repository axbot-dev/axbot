package com.github.axiangcoding.axbot.kook.service;

import com.github.axiangcoding.axbot.kook.service.entity.GuildListResp;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GuildService {

    @GET("/api/v3/guild/list")
    Call<GuildListResp> getList(@Query("page") Integer page,
                                @Query("page_size") Integer pageSize,
                                @Query("sort") String sort);
}
