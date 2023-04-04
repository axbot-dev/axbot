package com.github.axiangcoding.axbot.bot.kook.service;

import com.github.axiangcoding.axbot.bot.kook.service.entity.resp.UserViewResp;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("/api/v3/user/view")
    Call<UserViewResp> getView(
            @Query("user_id") String userId,
            @Query("guild_id") String guildId
    );
}
