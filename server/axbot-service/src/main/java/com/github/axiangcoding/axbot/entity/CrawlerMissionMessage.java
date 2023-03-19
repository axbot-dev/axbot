package com.github.axiangcoding.axbot.entity;

import lombok.Data;

@Data
public class CrawlerMissionMessage {
    String missionId;
    String url;
    String xpathCondition;
}
