package com.github.axiangcoding.axbot.server.schedule;

import com.github.axiangcoding.axbot.remote.bilibili.BiliClient;
import com.github.axiangcoding.axbot.remote.bilibili.service.entity.BiliResponse;
import com.github.axiangcoding.axbot.remote.bilibili.service.entity.resp.RoomInfoData;
import com.github.axiangcoding.axbot.server.util.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
public class ScheduleTask {

    @Resource
    BiliClient biliClient;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void checkBiliRoom() {
        log.info("check bilibili room");
        BiliResponse<RoomInfoData> liveRoomInfo = biliClient.getLiveRoomInfo("5295790");
        System.out.println(JsonUtils.toJson(liveRoomInfo));
    }

    @Scheduled(cron = "@daily")
    public void cleanUsage(){
        log.info("clean usage daily");
    }


}
