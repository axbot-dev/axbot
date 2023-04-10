package com.github.axiangcoding.axbot.remote.kook.service;


import com.github.axiangcoding.axbot.remote.kook.service.entity.KookResponse;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookGuild;
import com.github.axiangcoding.axbot.remote.kook.service.entity.resp.GuildListData;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GuildService {

    @GET("/api/v3/guild/list")
    Single<KookResponse<GuildListData>> getGuildList(
            @Query("page") Integer page,
            @Query("page_size") Integer pageSize,
            @Query("sort") String sort);

    @GET("/api/v3/guild/view")
    Single<KookResponse<KookGuild>> getGuildView(
            @Query("guild_id") String guildId);
}
