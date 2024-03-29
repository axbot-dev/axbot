package com.github.axiangcoding.axbot.crawler.wt.parser;

import com.github.axiangcoding.axbot.crawler.wt.entity.NewParseResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WtNewsParser {
    public static List<NewParseResult> parseUrl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return parse(doc);
    }

    private static List<NewParseResult> parse(Document document) {
        ArrayList<NewParseResult> list = new ArrayList<>();

        Elements elements = document.select("div[class~=showcase__item]");
        String baseUrl = "https://warthunder.com";
        elements.forEach(item ->{
            NewParseResult parseResult = new NewParseResult();
            String newsUrl = baseUrl + item.select("a[class=widget__link]").attr("href");
            String posterUrl = "https:" +item.select("div[class=widget__poster]>img[class~=widget__poster-media]").get(0).attr("data-src");
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
