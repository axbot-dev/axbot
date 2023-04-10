package com.github.axiangcoding.axbot.remote.bilibili;

import com.github.axiangcoding.axbot.remote.bilibili.service.LiveRoomService;
import com.github.axiangcoding.axbot.remote.bilibili.service.entity.BiliResponse;
import com.github.axiangcoding.axbot.remote.bilibili.service.entity.resp.RoomInfoData;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Slf4j
public class BiliClient {
    private final LiveRoomService liveRoomService;
    private static final String LIVE_BASE_URL = "https://api.live.bilibili.com/";

    public BiliClient() {
        Retrofit retrofit = initRetrofit();
        this.liveRoomService = retrofit.create(LiveRoomService.class);
    }

    private Retrofit initRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE));
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder builder1 = original.newBuilder()
                    .addHeader("Content-Type", "application/json");
            Request request = builder1.build();
            return chain.proceed(request);
        });

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(LIVE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                .create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build());

        return builder.build();
    }

    private <T> BiliResponse<T> execute(Single<BiliResponse<T>> apiCall) {
        try {
            BiliResponse<T> response = apiCall.blockingGet();
            if (response.getCode() != 0) {
                log.warn("response business code: {}, message: {}", response.getCode(), response.getMessage());
            }
            return response;
        } catch (HttpException e) {
            try {
                if (e.response() == null || e.response().errorBody() == null) {
                    throw e;
                }
                String errorBody = e.response().errorBody().string();
                log.warn("execute error: {}", errorBody);
                throw new RuntimeException(e);
            } catch (IOException ex) {
                // couldn't parse error
                throw e;
            }
        }
    }

    public BiliResponse<RoomInfoData> getLiveRoomInfo(String roomId) {
        return execute(liveRoomService.getRoomInfo(roomId));
    }
}
