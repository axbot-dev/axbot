package com.github.axiangcoding.axbot.bot.kook.service.entity.resp;

import com.github.axiangcoding.axbot.bot.kook.service.entity.CommonMeta;
import com.github.axiangcoding.axbot.bot.kook.service.entity.CommonResp;
import com.github.axiangcoding.axbot.bot.kook.service.entity.CommonRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class GuildRoleListResp extends CommonResp {
    DataItem data;
    @Getter
    @Setter
    public static class DataItem {
        List<CommonRole> items;
        CommonMeta meta;
        Map<String, Integer> sort;
    }
}
