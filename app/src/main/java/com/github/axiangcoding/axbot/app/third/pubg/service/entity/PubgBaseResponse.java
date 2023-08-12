package com.github.axiangcoding.axbot.app.third.pubg.service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class PubgBaseResponse {
    Links links;
    Meta meta;

    @Getter
    @Setter
    @ToString
    public static class Links {
        String self;
        // String next;
        // String last;
    }

    @Getter
    @Setter
    @ToString
    public static class Meta {
        // Integer status;
        // String message;
        // Integer total;
        // Integer totalPages;
        // Integer currentPages;
        // Integer limit;
    }
}
