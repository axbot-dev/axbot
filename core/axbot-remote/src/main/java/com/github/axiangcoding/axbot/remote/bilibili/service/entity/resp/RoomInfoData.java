package com.github.axiangcoding.axbot.remote.bilibili.service.entity.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * TODO 只解析需要的字段
 * 更多字段可见 <a href="https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/live/info.md#%E8%8E%B7%E5%8F%96%E7%9B%B4%E6%92%AD%E9%97%B4%E4%BF%A1%E6%81%AF">文档</a>
 */
@Getter
@Setter
@ToString
public class RoomInfoData {
    Long uid;
    Long roomId;
    String description;
    String title;
    Integer liveStatus;
    String areaName;

}
