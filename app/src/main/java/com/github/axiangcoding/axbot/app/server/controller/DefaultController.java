package com.github.axiangcoding.axbot.app.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {
    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}
