package com.github.axiangcoding.app.crawler.entity;

import lombok.Data;

@Data
public class NewParseResult {
    String title;
    String comment;
    String url;
    String posterUrl;
    String dateStr;
}
