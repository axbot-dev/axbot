package com.github.axiangcoding.axbot.server.configuration;

import com.github.axiangcoding.axbot.remote.bilibili.BiliClient;
import com.github.axiangcoding.axbot.remote.cqhttp.CqHttpClient;
import com.github.axiangcoding.axbot.remote.kook.KookClient;

import com.github.axiangcoding.axbot.remote.qiniu.QiniuClient;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoteServerConfiguration {
    @Resource
    BotConfProps botConfProps;

    @Bean
    public KookClient kookClient() {
        return new KookClient(botConfProps.getKook().getBotToken());
    }

    @Bean
    public BiliClient biliClient() {
        return new BiliClient();
    }

    @Bean
    public CqHttpClient cqHttpClient() {
        return new CqHttpClient(botConfProps.getCqhttp().getBaseUrl());
    }

    @Bean
    public QiniuClient qiniuClient() {
        BotConfProps.Censor censor = botConfProps.getCensor();
        return new QiniuClient(censor.getQiniu().getAccessToken(), censor.getQiniu().getSecretToken());
    }
}
