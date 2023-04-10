package com.github.axiangcoding.axbot.remote.kook.service;

import com.github.axiangcoding.axbot.remote.kook.service.entity.KookResponse;
import com.github.axiangcoding.axbot.remote.kook.service.entity.req.CreateMessageReq;
import com.github.axiangcoding.axbot.remote.kook.service.entity.resp.CreateMessageData;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MessageService {
    @POST("/api/v3/message/create")
    Single<KookResponse<CreateMessageData>> createMessage(
            @Body CreateMessageReq req);
}
