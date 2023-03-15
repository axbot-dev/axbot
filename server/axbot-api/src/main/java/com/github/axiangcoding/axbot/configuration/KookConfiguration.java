package com.github.axiangcoding.axbot.configuration;

import com.github.axiangcoding.axbot.configuration.props.KookConfProps;
import com.github.axiangcoding.axbot.kook.KookClient;
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
