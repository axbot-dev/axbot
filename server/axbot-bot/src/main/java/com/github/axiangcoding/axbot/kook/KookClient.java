package com.github.axiangcoding.axbot.kook;

import com.github.axiangcoding.axbot.kook.service.GuildService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KookClient {
    private final GuildService guildService;
    private static final String BASE_URL = "https://www.kookapp.cn/";

    private Retrofit initRetrofit(String botToken) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder builder1 = original.newBuilder()
                    .addHeader("Authorization", botToken)
                    .addHeader("Content-Type", "application/json");
            Request request = builder1.build();
            return chain.proceed(request);
        });

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                .create())).client(httpClient.build());

        return builder.build();
    }

    public KookClient(String botToken) {
        if (!botToken.startsWith("Bot")) {
            botToken = "Bot " + botToken;
        }
        Retrofit retrofit = initRetrofit(botToken);
        this.guildService = retrofit.create(GuildService.class);
    }

    public GuildService guildService() {
        return guildService;
    }


}
