package com.github.axiangcoding.axbot.app.crawler;

import com.github.axiangcoding.axbot.app.crawler.entity.WTNewParseResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WtCrawlerClientTest {
    private WtCrawlerClient wtCrawlerClient;


    @BeforeEach
    void setUp() {
        wtCrawlerClient = new WtCrawlerClient();
    }

    @Test
    void testGetNewsFromEn() throws IOException {
        List<WTNewParseResult> newsFromUrl = wtCrawlerClient.getNewsFromUrl(WtCrawlerClient.REGION.EN);
        assertEquals(18, newsFromUrl.size());
    }

    @Test
    void testGetNewsFromZh() throws IOException {
        List<WTNewParseResult> newsFromUrl = wtCrawlerClient.getNewsFromUrl(WtCrawlerClient.REGION.ZH);
        assertEquals(18, newsFromUrl.size());
    }

}