package com.github.axiangcoding.axbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api")
public class RootController {

    @GetMapping("/health")
    public String health(){
        return "ok\n";
    }
}
