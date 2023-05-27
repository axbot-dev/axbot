package com.github.axiangcoding.axbot.remote.kook.service;

import com.github.axiangcoding.axbot.remote.kook.service.entity.KookResponse;
import com.github.axiangcoding.axbot.remote.kook.service.entity.req.CreateDirectMsgReq;
import com.github.axiangcoding.axbot.remote.kook.service.entity.resp.CreateMessageData;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DirectMessageService {
    @POST("/api/v3/direct-message/create")
    Single<KookResponse<CreateMessageData>> createDirectMessage(
            @Body CreateDirectMsgReq req);
}
