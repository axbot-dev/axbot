package com.github.axiangcoding.axbot.app.server.configuration;


import com.github.axiangcoding.axbot.app.crawler.WtCrawlerClient;
import com.github.axiangcoding.axbot.app.server.configuration.props.AppConfProps;
import com.github.axiangcoding.axbot.app.third.botmarket.BotMarketClient;
import com.github.axiangcoding.axbot.app.third.pubg.PubgClient;
import com.github.axiangcoding.axbot.app.third.qiniu.QiniuClient;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThirdClientConfiguration {
    @Resource
    AppConfProps appConfProps;

    @Bean
    public WtCrawlerClient wtCrawlerClient() {
        return new WtCrawlerClient();
    }

    @Bean
    public QiniuClient qiniuClient() {
        AppConfProps.Censor censor = appConfProps.getCensor();
        return new QiniuClient(censor.getQiniu().getAccessToken(), censor.getQiniu().getSecretToken());
    }

    @Bean
    public BotMarketClient botMarketClient() {
        return new BotMarketClient();
    }

    @Bean
    public PubgClient pubgClient() {
        return new PubgClient(appConfProps.getPubgApi().getApiKey());
    }
}
