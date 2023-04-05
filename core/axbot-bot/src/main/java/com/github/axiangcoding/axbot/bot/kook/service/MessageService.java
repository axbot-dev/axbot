package com.github.axiangcoding.axbot.bot.kook.service;

import com.github.axiangcoding.axbot.bot.kook.service.entity.req.CreateMessageReq;
import com.github.axiangcoding.axbot.bot.kook.service.entity.resp.CreateMessageResp;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MessageService {
    @POST("/api/v3/message/create")
    Single<CreateMessageResp> createMessage(
            @Body CreateMessageReq req);
}
