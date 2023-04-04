package com.github.axiangcoding.axbot.bot.kook.service;

import com.github.axiangcoding.axbot.bot.kook.service.entity.resp.GuildRoleListResp;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GuildRoleService {
    @GET("/api/v3/guild-role/list")
    Call<GuildRoleListResp> getView(
            @Query("guild_id") String guildId,
            @Query("page") Integer page,
            @Query("page_size") Integer pageSize
    );

}
