package com.github.axiangcoding.axbot.bot.kook.service;

import com.github.axiangcoding.axbot.bot.kook.service.entity.CreateMessageReq;
import com.github.axiangcoding.axbot.bot.kook.service.entity.CreateMessageResp;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MessageService {
    @POST("/api/v3/message/create")
    Call<CreateMessageResp> createMessage(
            @Body CreateMessageReq req);
}
