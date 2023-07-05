package com.github.axiangcoding.axbot.app.third.qiniu.service.entity.resp;

import lombok.Data;

import java.util.List;

/**
 * 介绍于 <a href="https://developer.qiniu.com/censor/7260/api-text-censor">API文档</a>
 */
@Data
public class TextCensorResult {
    String suggestion;
    Scense scenes;

    @Data
    public static class Scense {
        Antispam antispam;


        @Data
        public static class Antispam {
            String suggestion;
            List<Detail> details;

            @Data
            public static class Detail {
                String label;
                String score;
                List<Object> contexts;
            }
        }
    }


}
