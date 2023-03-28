package com.github.axiangcoding.axbot.server.controller.v1;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
//@RestController
public class MessageController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    Queue outQueue;

    @PostMapping("/messages")
    public void sendMessage(@RequestBody String message) {
        rabbitTemplate.convertAndSend(outQueue.getName(), message);
        log.info("send a message");
        System.out.println(" [x] Sent '" + message + "' to " + outQueue.getName());
    }

}
