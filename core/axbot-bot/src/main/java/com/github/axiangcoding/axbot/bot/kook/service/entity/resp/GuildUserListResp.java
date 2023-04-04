package com.github.axiangcoding.axbot.bot.kook.service.entity.resp;

import com.github.axiangcoding.axbot.bot.kook.service.entity.CommonMeta;
import com.github.axiangcoding.axbot.bot.kook.service.entity.CommonResp;
import com.github.axiangcoding.axbot.bot.kook.service.entity.CommonUser;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GuildUserListResp extends CommonResp {
    DataItem data;

    @Getter
    @Setter
    public static class DataItem {
        List<CommonUser> items;
        CommonMeta meta;
        Map<String, Integer> sort;
    }
}
