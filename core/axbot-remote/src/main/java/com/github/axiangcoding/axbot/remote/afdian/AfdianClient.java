package com.github.axiangcoding.axbot.remote.afdian;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.github.axiangcoding.axbot.remote.afdian.service.ApiService;
import com.github.axiangcoding.axbot.remote.afdian.service.entity.AfdianResponse;
import com.github.axiangcoding.axbot.remote.afdian.service.entity.req.QueryOrderReq;
import com.github.axiangcoding.axbot.remote.afdian.service.entity.resp.QueryOrderResp;
import com.github.axiangcoding.axbot.remote.util.MD5Utils;
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
import java.time.Instant;

@Slf4j
public class AfdianClient {
    private static final String BASE_URL = "https://afdian.net/";

    private final ApiService apiService;
    private final String userId;
    private final String token;

    public AfdianClient(String userId, String token) {
        Retrofit retrofit = initRetrofit();
        if (userId == null || token == null) {
            log.warn("userId or token is null");
            this.userId = null;
            this.token = null;
            this.apiService = null;
            return;
        }
        this.userId = userId;
        this.token = token;
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

    private <T> AfdianResponse<T> execute(Single<AfdianResponse<T>> apiCall) {
        try {
            AfdianResponse<T> response = apiCall.blockingGet();
            if (response.getEc() != 200) {
                log.warn("response business code: {}, message: {}", response.getEc(), response.getEm());
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

    public AfdianResponse<QueryOrderResp> queryOrder(Integer page, String outTradeNo) {
        JSONObject paramsJson = new JSONObject();
        if (page != null) {
            paramsJson.put("page", page);
        }
        if (outTradeNo != null) {
            paramsJson.put("out_trade_no", outTradeNo);
        }
        String params = paramsJson.toJSONString(JSONWriter.Feature.MapSortField);
        long ts = Instant.now().getEpochSecond();
        String signVal = "%sparams%sts%duser_id%s".formatted(token, params, ts, this.userId);
        String sign = MD5Utils.calculateMD5(signVal);

        QueryOrderReq req=  new QueryOrderReq();
        req.setUserId(this.userId);
        req.setParams(params);
        req.setTs(ts);
        req.setSign(sign);

        return execute(apiService.queryOrder(req));
    }
}
