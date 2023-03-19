package com.github.axiangcoding.axbot.configuration;

import com.github.axiangcoding.axbot.service.MessageQueueService;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfiguration {

    @Bean
    public Queue inQueue() {
        return new Queue(MessageQueueService.IN_QUEUE_NAME);
    }

    @Bean
    public Queue outQueue() {
        return new Queue(MessageQueueService.OUT_QUEUE_NAME);
    }
}
