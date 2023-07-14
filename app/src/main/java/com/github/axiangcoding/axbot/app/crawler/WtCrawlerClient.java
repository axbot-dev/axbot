package com.github.axiangcoding.axbot.app.crawler;

import com.github.axiangcoding.axbot.app.crawler.entity.ProfileParseResult;
import com.github.axiangcoding.axbot.app.crawler.entity.WTNewParseResult;
import com.github.axiangcoding.axbot.app.crawler.parser.WtGamerProfileParser;
import com.github.axiangcoding.axbot.app.crawler.parser.WtNewsParser;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

public class WtCrawlerClient {
    @AllArgsConstructor
    @Getter
    public enum MODE {
        DIRECT("direct"),
        SELENIUM("selenium"),
        AUTO("auto");
        private final String name;
    }

    @AllArgsConstructor
    @Getter
    public enum REGION {
        EN("en"),
        ZH("zh");
        private final String name;
    }

    private static final String GAMER_PROFILE_URL_TEMPLATE = "https://warthunder.com/zh/community/userinfo/?nick=%s";
    private static final String NEWS_FIRST_PAGE_URL_TEMPLATE = "https://warthunder.com/%s/news/";

    public static final String EXIST_PROFILE_XPATH_CONDITION = "//div[@class=\"content__title\"]";

    public static String formatGetProfileUrl(String nickname) {
        return GAMER_PROFILE_URL_TEMPLATE.formatted(nickname);
    }

    public ProfileParseResult getProfileFromUrl(String nickname) throws IOException {
        String url = formatGetProfileUrl(nickname);
        return WtGamerProfileParser.parseUrl(url);
    }

    public ProfileParseResult getProfileFromHtml(String html) {
        return WtGamerProfileParser.parseHtml(html);
    }

    public List<WTNewParseResult> getNewsFromUrl(REGION region) throws IOException {
        return WtNewsParser.parseUrl(NEWS_FIRST_PAGE_URL_TEMPLATE.formatted(region.getName()));
    }
}
