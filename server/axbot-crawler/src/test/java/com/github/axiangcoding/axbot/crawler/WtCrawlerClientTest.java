package com.github.axiangcoding.axbot.crawler;


import com.github.axiangcoding.axbot.crawler.entity.ParserResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class WtCrawlerClientTest {
    @Test
    public void getProfileFromHtml1() throws IOException {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("user_not_exist_origin.html");
        Assert.assertNotNull(resourceAsStream);
        String pageSource = new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);

        WtCrawlerClient wtCrawlerClient = new WtCrawlerClient();
        ParserResult parserResult = wtCrawlerClient.getProfileFromHtml(pageSource);
        Assert.assertEquals(parserResult.getFound(), false);
    }

    @Test
    public void getProfileFromHtml2() throws IOException {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("user_exist_origin.html");
        Assert.assertNotNull(resourceAsStream);
        String pageSource = new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);

        WtCrawlerClient wtCrawlerClient = new WtCrawlerClient();
        ParserResult parserResult = wtCrawlerClient.getProfileFromHtml(pageSource);
        Assert.assertEquals(parserResult.getFound(), true);
    }
}