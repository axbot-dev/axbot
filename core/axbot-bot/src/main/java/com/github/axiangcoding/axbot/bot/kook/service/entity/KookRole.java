package com.github.axiangcoding.axbot.bot.kook.service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KookRole {
    Long roleId;
    String name;
    Long color;
    Integer position;
    Integer hoist;
    Integer mentionable;
    Long permissions;
}
