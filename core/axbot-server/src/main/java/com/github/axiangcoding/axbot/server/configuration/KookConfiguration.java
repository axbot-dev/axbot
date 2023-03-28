package com.github.axiangcoding.axbot.server.configuration;

import com.github.axiangcoding.axbot.bot.kook.KookClient;
import com.github.axiangcoding.axbot.server.configuration.props.KookConfProps;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KookConfiguration {
    @Resource
    KookConfProps kookConfProps;

    @Bean
    public KookClient kookClient() {
        return new KookClient(kookConfProps.getBotToken());
    }
}
