package com.github.axiangcoding.axbot.app.third.botmarket.service;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiService {

    @GET("/api/v1/online.bot")
    Single<Object> setOnline(@Header("uuid") String uuid);
}
