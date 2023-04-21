package com.github.axiangcoding.axbot.remote.qiniu.service;

import com.github.axiangcoding.axbot.remote.qiniu.service.entity.QiniuResponse;
import com.github.axiangcoding.axbot.remote.qiniu.service.entity.resp.TextCensorResult;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CensorService {
    @POST("/v3/text/censor")
    Single<QiniuResponse<TextCensorResult>> postTextCensor(
            @Body String body,
            @Header("Authorization") String authorization);
}
