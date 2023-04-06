package com.github.axiangcoding.axbot.bot.kook.service.entity.resp;

import com.github.axiangcoding.axbot.bot.kook.service.entity.KookMeta;
import com.github.axiangcoding.axbot.bot.kook.service.entity.KookGuild;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class GuildListData {
    List<KookGuild> items;
    KookMeta meta;
    Map<String, Integer> sort;

}
