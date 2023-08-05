package com.github.axiangcoding.axbot.app.server.service.entity;

import lombok.Data;

@Data
public class CrawlerResultMessage {
    String missionId;
    String pageSource;
    Double timeUsage;
}
