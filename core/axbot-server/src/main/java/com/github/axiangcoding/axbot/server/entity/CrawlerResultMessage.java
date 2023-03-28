package com.github.axiangcoding.axbot.server.entity;

import lombok.Data;

@Data
public class CrawlerResultMessage {
    String missionId;
    String pageSource;
    Integer timeUsage;
}
