package com.github.axiangcoding.axbot.bot.kook.service.entity.resp;

import com.github.axiangcoding.axbot.bot.kook.service.entity.KookMeta;
import com.github.axiangcoding.axbot.bot.kook.service.entity.KookUser;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GuildUserListData {
    List<KookUser> items;
    KookMeta meta;
    Map<String, Integer> sort;
}
