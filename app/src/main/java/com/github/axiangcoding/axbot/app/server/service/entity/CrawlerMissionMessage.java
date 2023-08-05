package com.github.axiangcoding.axbot.app.server.service.entity;

import lombok.Data;

@Data
public class CrawlerMissionMessage {
    String missionId;
    String url;
    String xpathCondition;
}
