package com.github.axiangcoding.axbot.bot.kook;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.axiangcoding.axbot.bot.kook.service.GuildRoleService;
import com.github.axiangcoding.axbot.bot.kook.service.GuildService;
import com.github.axiangcoding.axbot.bot.kook.service.MessageService;
import com.github.axiangcoding.axbot.bot.kook.service.UserService;
import com.github.axiangcoding.axbot.bot.kook.service.entity.req.CreateMessageReq;
import com.github.axiangcoding.axbot.bot.kook.service.entity.resp.CreateMessageResp;
import com.github.axiangcoding.axbot.bot.kook.service.entity.resp.GuildRoleListResp;
import com.github.axiangcoding.axbot.bot.kook.service.entity.resp.GuildViewResp;
import com.github.axiangcoding.axbot.bot.kook.service.entity.resp.UserViewResp;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

@Slf4j
public class KookClient {
    private final GuildService guildService;
    private final MessageService messageService;
    private final UserService userService;
    private final GuildRoleService guildRoleService;

    private static final String BASE_URL = "https://www.kookapp.cn/";

    private Retrofit initRetrofit(String botToken) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
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
                .addConverterFactory(JacksonConverterFactory.create(
                        new ObjectMapper()
                                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                ))
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
                // couldn't parse error
                throw e;
            }
        }
    }

    public KookClient(String botToken) {
        if (!botToken.startsWith("Bot")) {
            botToken = "Bot " + botToken;
        }
        Retrofit retrofit = initRetrofit(botToken);
        this.guildService = retrofit.create(GuildService.class);
        this.messageService = retrofit.create(MessageService.class);
        this.userService = retrofit.create(UserService.class);
        this.guildRoleService = retrofit.create(GuildRoleService.class);
    }

    public CreateMessageResp createMessage(CreateMessageReq req) {
        return execute(messageService.createMessage(req));
    }

    public GuildViewResp getGuildView(String guildId) {
        return execute(guildService.getGuildView(guildId));
    }

    public UserViewResp getUserView(String userId, String guildId) {
        return execute(userService.getView(userId, guildId));
    }

    public GuildRoleListResp getGuildRoleList(String guildId, Integer page, Integer pageSize) {
        return execute(guildRoleService.getView(guildId, page, pageSize));
    }
}
