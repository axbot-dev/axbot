package com.github.axiangcoding.axbot.configuration;

import com.github.axiangcoding.axbot.crawler.WtCrawlerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrawlerConfiguration {
    @Bean
    public WtCrawlerClient wtCrawlerClient() {
        return new WtCrawlerClient();
    }
}
