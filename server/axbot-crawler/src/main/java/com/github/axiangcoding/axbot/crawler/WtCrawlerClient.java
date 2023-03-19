package com.github.axiangcoding.axbot.crawler;

import com.github.axiangcoding.axbot.crawler.entity.ParserResult;
import com.github.axiangcoding.axbot.crawler.parser.WtGamerProfileParser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class WtCrawlerClient {
    public static final String MODE_DIRECT = "direct";
    public static final String MODE_SELENIUM = "selenium";
    public static final String MODE_AUTO = "auto";

    private static final String GAMER_PROFILE_URL_TEMPLATE = "https://warthunder.com/zh/community/userinfo/?nick=%s";


    public static final String EXIST_XPATH_CONDITION = "//div[@class=\"content__title\"]";

    public static String formatGetProfileUrl(String nickname) {
        return GAMER_PROFILE_URL_TEMPLATE.formatted(nickname);
    }


    public WtCrawlerClient() {

    }

    public ParserResult getProfileFromUrl(String nickname) throws IOException {
        String url = formatGetProfileUrl(nickname);
        ParserResult o = WtGamerProfileParser.parseUrl(url);
        return o;
    }

    public ParserResult getProfileFromHtml(String html) {
        ParserResult o = WtGamerProfileParser.parseHtml(html);
        return o;
    }
}
