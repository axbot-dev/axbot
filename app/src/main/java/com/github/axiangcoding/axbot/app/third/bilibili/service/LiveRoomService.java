package com.github.axiangcoding.axbot.app.third.bilibili.service;


import com.github.axiangcoding.axbot.app.third.bilibili.service.entity.BiliResponse;
import com.github.axiangcoding.axbot.app.third.bilibili.service.entity.resp.RoomInfoData;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LiveRoomService {
    @GET("/room/v1/Room/get_info")
    Single<BiliResponse<RoomInfoData>> getRoomInfo(@Query("room_id") String roomId);
}
