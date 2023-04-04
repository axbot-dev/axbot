package com.github.axiangcoding.axbot.bot.kook.service;


import com.github.axiangcoding.axbot.bot.kook.service.entity.resp.GuildListResp;
import com.github.axiangcoding.axbot.bot.kook.service.entity.resp.GuildViewResp;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GuildService {

    @GET("/api/v3/guild/list")
    Call<GuildListResp> getGuildList(
            @Query("page") Integer page,
            @Query("page_size") Integer pageSize,
            @Query("sort") String sort);

    @GET("/api/v3/guild/view")
    Call<GuildViewResp> getGuildView(
            @Query("guild_id") String guildId);
}
