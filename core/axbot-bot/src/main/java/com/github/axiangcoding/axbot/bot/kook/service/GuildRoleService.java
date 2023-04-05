package com.github.axiangcoding.axbot.bot.kook.service;

import com.github.axiangcoding.axbot.bot.kook.service.entity.resp.GuildRoleListResp;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GuildRoleService {
    @GET("/api/v3/guild-role/list")
    Single<GuildRoleListResp> getView(
            @Query("guild_id") String guildId,
            @Query("page") Integer page,
            @Query("page_size") Integer pageSize
    );

}
