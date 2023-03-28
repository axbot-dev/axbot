package com.github.axiangcoding.axbot.server.entity;

import lombok.Data;

@Data
public class CrawlerMissionMessage {
    String missionId;
    String url;
    String xpathCondition;
}
