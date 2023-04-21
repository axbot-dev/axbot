package com.github.axiangcoding.axbot.remote.cqhttp;


import com.github.axiangcoding.axbot.remote.cqhttp.service.MessageService;
import com.github.axiangcoding.axbot.remote.cqhttp.service.entity.CqhttpResponse;
import com.github.axiangcoding.axbot.remote.cqhttp.service.entity.resp.SendGroupMsgData;
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
public class CqHttpClient {
    private final String baseUrl;

    private final MessageService messageService;

    public CqHttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
        Retrofit retrofit = initRetrofit();
        this.messageService = retrofit.create(MessageService.class);
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
                .baseUrl(this.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                .create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build());

        return builder.build();
    }

    private <T> CqhttpResponse<T> execute(Single<CqhttpResponse<T>> apiCall) {
        try {
            CqhttpResponse<T> response = apiCall.blockingGet();
            if (response.getRetcode() != 0 && response.getRetcode() != 1) {
                log.warn("response business code: {}, msg: {}, wording: {}",
                        response.getRetcode(), response.getMsg(), response.getWording());
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
                throw e;
            }
        }
    }

    public CqhttpResponse<SendGroupMsgData> sendGroupMsg(Long groupId, String message, Boolean autoEscape) {
        return execute(messageService.sendGroupMsg(groupId, message, autoEscape));
    }
}
