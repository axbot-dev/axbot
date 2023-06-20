package com.github.axiangcoding.axbot.remote.afdian.service;

import com.github.axiangcoding.axbot.remote.afdian.service.entity.AfdianResponse;
import com.github.axiangcoding.axbot.remote.afdian.service.entity.req.QueryOrderReq;
import com.github.axiangcoding.axbot.remote.afdian.service.entity.resp.QueryOrderResp;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/open/query-order")
    Single<AfdianResponse<QueryOrderResp>> queryOrder(@Body QueryOrderReq body);

    @POST("/api/open/ping")
    Single<AfdianResponse<Object>> ping(@Body Object body);
}
