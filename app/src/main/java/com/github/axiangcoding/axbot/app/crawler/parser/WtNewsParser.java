package com.github.axiangcoding.axbot.app.crawler.parser;


import com.github.axiangcoding.axbot.app.crawler.entity.WTNewParseResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WtNewsParser {
    public static List<WTNewParseResult> parseUrl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return parse(doc);
    }

    private static List<WTNewParseResult> parse(Document document) {
        ArrayList<WTNewParseResult> list = new ArrayList<>();

        Elements elements = document.select("div[class~=showcase__item]");
        String baseUrl = "https://warthunder.com";
        elements.forEach(item -> {
            WTNewParseResult parseResult = new WTNewParseResult();
            String newsUrl = baseUrl + item.select("a[class=widget__link]").attr("href");
            String posterUrl = "https:" + item.select("div[class=widget__poster]>img[class~=widget__poster-media]").get(0).attr("data-src");
            String title = item.select("div[class~=widget__content]>div[class~=widget__title]").get(0).text();
            String comment = item.select("div[class~=widget__content]>div[class~=widget__comment]").get(0).text();
            String dateStr = item.select("ul[class~=widget__meta]>li[class~=widget-meta__item--right]").text();

            parseResult.setUrl(newsUrl);
            parseResult.setPosterUrl(posterUrl);
            parseResult.setTitle(title);
            parseResult.setComment(comment);
            parseResult.setDateStr(dateStr);

            list.add(parseResult);

        });
        return list;
    }
}
