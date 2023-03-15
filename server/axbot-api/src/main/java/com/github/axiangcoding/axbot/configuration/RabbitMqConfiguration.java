package com.github.axiangcoding.axbot.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfiguration {
    public static final String IN_QUEUE_NAME = "crawler_result";
    public static final String OUT_QUEUE_NAME = "crawler_task";

    @Bean
    public Queue inQueue() {
        return new Queue(IN_QUEUE_NAME);
    }

    @Bean
    public Queue outQueue() {
        return new Queue(OUT_QUEUE_NAME);
    }
}
