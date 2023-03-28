package com.github.axiangcoding.axbot.server.configuration;

import com.github.axiangcoding.axbot.engine.AxBotEngine;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AxBotEngineConfiguration {
    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Bean
    public AxBotEngine axBotEngine() {
        return new AxBotEngine(threadPoolTaskExecutor.getThreadPoolExecutor());
    }
}
