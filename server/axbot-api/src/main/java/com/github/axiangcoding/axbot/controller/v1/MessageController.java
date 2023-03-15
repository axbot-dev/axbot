package com.github.axiangcoding.axbot.controller.v1;

import com.github.axiangcoding.axbot.configuration.RabbitMqConfiguration;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    Queue outQueue;

    @PostMapping("/messages")
    public void sendMessage(@RequestBody String message) {
        rabbitTemplate.convertAndSend(outQueue.getName(), message);
        System.out.println(" [x] Sent '" + message + "' to " + outQueue.getName());
    }

    @RabbitListener(queues = RabbitMqConfiguration.IN_QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }
}
