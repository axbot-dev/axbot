package com.github.axiangcoding.axbot.bot.kook.service.entity.resp;

import com.github.axiangcoding.axbot.bot.kook.service.entity.CommonMeta;
import com.github.axiangcoding.axbot.bot.kook.service.entity.CommonResp;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
@Setter
public class GuildListResp extends CommonResp {
    DataItem data;

    @Getter
    @Setter
    public static class DataItem {
        List<Item> items;
        CommonMeta meta;
        Map<String, Integer> sort;
    }

    @Getter
    @Setter
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


}
