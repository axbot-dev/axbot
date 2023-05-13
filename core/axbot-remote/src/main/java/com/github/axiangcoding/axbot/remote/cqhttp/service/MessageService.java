package com.github.axiangcoding.axbot.remote.cqhttp.service;

import com.github.axiangcoding.axbot.remote.cqhttp.service.entity.CqhttpResponse;
import com.github.axiangcoding.axbot.remote.cqhttp.service.entity.resp.GroupMemberInfo;
import com.github.axiangcoding.axbot.remote.cqhttp.service.entity.resp.SendGroupMsgData;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MessageService {
    @GET("/send_group_msg")
    Single<CqhttpResponse<SendGroupMsgData>> sendGroupMsg(
            @Query("group_id") Long groupId,
            @Query("message") String message,
            @Query("auto_escape") Boolean autoEscape);

    @GET("/get_group_member_info")
    Single<CqhttpResponse<GroupMemberInfo>> getGroupMemberInfo(
            @Query("group_id") Long groupId,
            @Query("user_id") Long userId,
            @Query("no_cache") Boolean noCache
    );
}
