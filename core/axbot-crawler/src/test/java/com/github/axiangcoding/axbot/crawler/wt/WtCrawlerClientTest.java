package com.github.axiangcoding.axbot.crawler.wt;

import java.io.IOException;

class WtCrawlerClientTest {

    @org.junit.jupiter.api.Test
    void getNewsFromUrl() throws IOException {
        new WtCrawlerClient().getNewsFromUrl(WtCrawlerClient.REGION.ZH);
    }
}