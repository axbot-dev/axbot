package com.github.axiangcoding.axbot.app.third.pubg;


import com.github.axiangcoding.axbot.app.third.pubg.service.PlayerService;
import com.github.axiangcoding.axbot.app.third.pubg.service.entity.PubgResponse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
public class PubgClient {
    @AllArgsConstructor
    @Getter
    public enum PLATFORM {
        KAKAO("kakao"),
        STADIA("stadia"),
        STEAM("steam"),
        TOURNAMENT("tournament"),
        PSN("psn"),
        XBOX("xbox"),
        CONSOLE("console");

        private final String text;
    }

    private static final String BASE_URL = "https://api.pubg.com/";
    private final String apiKey;

    private final PlayerService playerService;

    public PubgClient(String apiKey) {
        this.apiKey = apiKey;
        Retrofit retrofit = initRetrofit();
        playerService = retrofit.create(PlayerService.class);
    }

    private Retrofit initRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder builder1 = original.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Accept", "application/vnd.api+json");
            Request request = builder1.build();
            return chain.proceed(request);
        });

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                .create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build());

        return builder.build();
    }

    private <T> PubgResponse<T> execute(Single<PubgResponse<T>> apiCall) {
        try {
            return apiCall.blockingGet();
        } catch (HttpException e) {
            try {
                if (e.response() == null || e.response().errorBody() == null) {
                    throw e;
                }
                String errorBody = e.response().errorBody().string();
                log.warn("execute error: {}", errorBody);
                throw new RuntimeException(e);
            } catch (IOException ex) {
                throw e;
            }
        }
    }

    public PubgResponse<Object> getPlayers(String platform, String playerNames, String playerIds) {
        return execute(playerService.getPlayers(platform, playerNames, playerIds));
    }
}
