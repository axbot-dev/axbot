package com.github.axiangcoding.axbot.remote.kook.service;

import com.github.axiangcoding.axbot.remote.kook.service.entity.KookResponse;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookUser;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("/api/v3/user/view")
    Single<KookResponse<KookUser>> getView(
            @Query("user_id") String userId,
            @Query("guild_id") String guildId
    );
}
