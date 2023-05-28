package com.github.axiangcoding.axbot.remote.kook;


import com.github.axiangcoding.axbot.remote.kook.service.*;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookGuild;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookResponse;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookUser;
import com.github.axiangcoding.axbot.remote.kook.service.entity.req.CreateDirectMsgReq;
import com.github.axiangcoding.axbot.remote.kook.service.entity.req.CreateMsgReq;
import com.github.axiangcoding.axbot.remote.kook.service.entity.resp.CreateMessageData;
import com.github.axiangcoding.axbot.remote.kook.service.entity.resp.GuildRoleListData;
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
public class KookClient {
    private final GuildService guildService;
    private final MessageService messageService;
    private final UserService userService;
    private final GuildRoleService guildRoleService;
    private final DirectMessageService directMessageService;

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
                                .create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build());

        return builder.build();
    }

    private <T> KookResponse<T> execute(Single<KookResponse<T>> apiCall) {
        try {
            KookResponse<T> response = apiCall.blockingGet();
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

    public KookClient(String botToken) {
        if (!botToken.startsWith("Bot")) {
            botToken = "Bot " + botToken;
        }
        Retrofit retrofit = initRetrofit(botToken);
        this.guildService = retrofit.create(GuildService.class);
        this.messageService = retrofit.create(MessageService.class);
        this.userService = retrofit.create(UserService.class);
        this.guildRoleService = retrofit.create(GuildRoleService.class);
        this.directMessageService = retrofit.create(DirectMessageService.class);
    }

    public KookResponse<CreateMessageData> createMessage(CreateMsgReq req) {
        return execute(messageService.createMessage(req));
    }

    public KookResponse<KookGuild> getGuildView(String guildId) {
        return execute(guildService.getGuildView(guildId));
    }

    public KookResponse<KookUser> getUserView(String userId, String guildId) {
        return execute(userService.getView(userId, guildId));
    }

    public KookResponse<GuildRoleListData> getGuildRoleList(String guildId, Integer page, Integer pageSize) {
        return execute(guildRoleService.getView(guildId, page, pageSize));
    }

    public KookResponse<CreateMessageData> createDirectMessage(CreateDirectMsgReq req) {
        return execute(directMessageService.createDirectMessage(req));
    }
}
