package com.github.axiangcoding.axbot.kook.service.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GuildListResp {
    String code;
    String message;
    DataItem data;

    @Data
    public static class DataItem {
        List<Item> items;
        Meta meta;
        Map<String, Integer> sort;
    }

    @Data
    public static class Item {
        String id;
        String name;
        String topic;
        String masterId;
        String userId;
        Boolean isMaster;
        String icon;
        String notifyType;
        String region;
        String enableOpen;
        String openId;
        String defaultChannelId;
        String welcomeChannelId;
        String boostNum;
        String level;
    }

    @Data
    public static class Meta {
        Integer page;
        Integer pageTotal;
        Integer pageSize;
        Integer total;
    }
}
