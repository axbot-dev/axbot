package com.github.axiangcoding.axbot.server.service.entity;

import lombok.Data;

@Data
public class CrawlerMissionMessage {
    String missionId;
    String url;
    String xpathCondition;
}
