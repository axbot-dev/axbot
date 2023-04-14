package com.github.axiangcoding.axbot.server.service.entity;

import lombok.Data;

@Data
public class CrawlerResultMessage {
    String missionId;
    String pageSource;
    Double timeUsage;
}
