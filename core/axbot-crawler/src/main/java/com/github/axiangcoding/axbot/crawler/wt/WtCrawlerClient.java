package com.github.axiangcoding.axbot.crawler.wt;

import com.github.axiangcoding.axbot.crawler.wt.entity.NewParseResult;
import com.github.axiangcoding.axbot.crawler.wt.entity.ProfileParseResult;
import com.github.axiangcoding.axbot.crawler.wt.parser.WtGamerProfileParser;
import com.github.axiangcoding.axbot.crawler.wt.parser.WtNewsParser;
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

    public static final String EXIST_XPATH_CONDITION = "//div[@class=\"content__title\"]";

    public static String formatGetProfileUrl(String nickname) {
        return GAMER_PROFILE_URL_TEMPLATE.formatted(nickname);
    }


    public WtCrawlerClient() {

    }

    public ProfileParseResult getProfileFromUrl(String nickname) throws IOException {
        String url = formatGetProfileUrl(nickname);
        return WtGamerProfileParser.parseUrl(url);
    }

    public ProfileParseResult getProfileFromHtml(String html) {
        return WtGamerProfileParser.parseHtml(html);
    }

    public List<NewParseResult> getNewsFromUrl(REGION region) throws IOException {
        return WtNewsParser.parseUrl(NEWS_FIRST_PAGE_URL_TEMPLATE.formatted(region.getName()));
    }
}
