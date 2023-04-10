package com.github.axiangcoding.axbot.remote.kook.service.entity.resp;

import com.github.axiangcoding.axbot.remote.kook.service.entity.KookMeta;
import com.github.axiangcoding.axbot.remote.kook.service.entity.KookRole;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GuildRoleListData {
    List<KookRole> items;
    KookMeta meta;
    Map<String, Integer> sort;
}
