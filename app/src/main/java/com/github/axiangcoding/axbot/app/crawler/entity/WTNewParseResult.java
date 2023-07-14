package com.github.axiangcoding.axbot.app.crawler.entity;

import lombok.Data;

@Data
public class WTNewParseResult {
    String title;
    String comment;
    String url;
    String posterUrl;
    String dateStr;
}
