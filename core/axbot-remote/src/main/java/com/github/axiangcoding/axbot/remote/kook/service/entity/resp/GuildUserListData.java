package com.github.axiangcoding.axbot.remote.kook.service.entity.resp;

import com.github.axiangcoding.axbot.remote.kook.service.entity.KookMeta;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookUser;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GuildUserListData {
    List<KookUser> items;
    KookMeta meta;
    Map<String, Integer> sort;
}
