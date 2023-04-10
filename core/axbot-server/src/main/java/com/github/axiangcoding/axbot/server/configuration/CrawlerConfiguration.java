package com.github.axiangcoding.axbot.server.configuration;


import com.github.axiangcoding.axbot.crawler.wt.WtCrawlerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrawlerConfiguration {
    @Bean
    public WtCrawlerClient wtCrawlerClient() {
        return new WtCrawlerClient();
    }
}
