package com.github.axiangcoding.axbot.bot.kook;


import com.github.axiangcoding.axbot.bot.kook.service.GuildService;
import com.github.axiangcoding.axbot.bot.kook.service.MessageService;
import com.github.axiangcoding.axbot.bot.kook.service.entity.CreateMessageReq;
import com.github.axiangcoding.axbot.bot.kook.service.entity.CreateMessageResp;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Slf4j
public class KookClient {
    private final GuildService guildService;
    private final MessageService messageService;
    private static final String BASE_URL = "https://www.kookapp.cn/";

    private Retrofit initRetrofit(String botToken) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
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
        this.messageService = retrofit.create(MessageService.class);
    }

    public CreateMessageResp createMessage(CreateMessageReq req) {
        try {
            Response<CreateMessageResp> execute = messageService.createMessage(req).execute();

            if (!execute.isSuccessful()) {
                log.warn("request failed, status code is {}, message is {}",
                        execute.code(), execute.errorBody() != null ? execute.errorBody().string() : null);
                return null;
            }
            return execute.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
