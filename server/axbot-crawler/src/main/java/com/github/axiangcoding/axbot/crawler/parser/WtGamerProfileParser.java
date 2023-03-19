package com.github.axiangcoding.axbot.crawler.parser;

import com.github.axiangcoding.axbot.crawler.entity.ParserResult;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WtGamerProfileParser {
    public static ParserResult parseHtml(String html) {

        Document doc = Jsoup.parse(html);
        return parse(doc);
    }

    public static ParserResult parseUrl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return parse(doc);
    }

    private static ParserResult parse(Document document) {
        ParserResult pr = new ParserResult();
        // 未找到该用户
        Elements e1 = document.select("div[class=user__unavailable]");
        if (e1.size() != 0) {
            pr.setFound(false);
            return pr;
        }
        pr.setFound(true);

        ParserResult.GamerProfile gp = new ParserResult.GamerProfile();
        gp.setNickname(document.select("li[class=user-profile__data-nick]").text());
        gp.setClan(document.select("a[class=user-profile__data-link]").text());

        String clanSuffix = document.select("a[class=user-profile__data-link]").attr("href");
        if (StringUtils.isNotBlank(clanSuffix)) {
            gp.setClanUrl("https://warthunder.com" + clanSuffix);
        }
        gp.setBanned(document.select("div[class=user-profile__data-nick--banned]").size() != 0);
        gp.setRegisterDate(parseRegisterDate(document.select("li[class=user-profile__data-regdate]").text()));

        pr.setProfile(gp);
        return pr;
    }

    /**
     * @param str 形如 “注册日期 14.10.2019”
     * @return
     */
    private static LocalDate parseRegisterDate(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String[] split = str.split(" ");
        String dateStr = split[1];
        return LocalDate.parse(dateStr, formatter);
    }
}
