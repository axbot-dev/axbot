package com.github.axiangcoding.axbot.remote.botmarket;


import com.github.axiangcoding.axbot.remote.botmarket.service.ApiService;
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

/**
 * BotMarket的请求客户端
 */
@Slf4j
public class BotMarketClient {
    private static final String BASE_URL = "http://bot.gekj.net/";

    private final ApiService apiService;

    public BotMarketClient() {
        Retrofit retrofit = initRetrofit();
        this.apiService = retrofit.create(ApiService.class);
    }

    private Retrofit initRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder builder1 = original.newBuilder()
                    .addHeader("Content-Type", "application/json");
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

    private <T> T execute(Single<T> apiCall) {
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

    public void setOnline(String uuid) {
        execute(apiService.setOnline(uuid));
    }

}
