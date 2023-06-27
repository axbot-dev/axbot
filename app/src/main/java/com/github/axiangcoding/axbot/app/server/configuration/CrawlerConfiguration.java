package com.github.axiangcoding.axbot.app.server.configuration;


import com.github.axiangcoding.axbot.app.crawler.WtCrawlerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrawlerConfiguration {
    @Bean
    public WtCrawlerClient wtCrawlerClient() {
        return new WtCrawlerClient();
    }
}
