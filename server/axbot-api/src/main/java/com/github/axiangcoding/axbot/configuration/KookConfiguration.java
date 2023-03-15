package com.github.axiangcoding.axbot.configuration;

import com.github.axiangcoding.axbot.kook.KookClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KookConfiguration {
    @Bean
    public KookClient kookClient() {
        KookClient kookClient = new KookClient("1/MTUyNTM=/ytpIjBl36wtq5HmGWDU/Ag==");
        return kookClient;
    }
}
