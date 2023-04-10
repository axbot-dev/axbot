package com.github.axiangcoding.axbot.remote.kook.service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KookMeta {
    Integer page;
    Integer pageTotal;
    Integer pageSize;
    Integer total;
}
