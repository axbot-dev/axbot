package com.github.axiangcoding.app.server.controller;

import com.github.axiangcoding.app.server.service.TestService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO 删除测试类
 */
@RestController
@Slf4j
@RequestMapping("v1/test")
public class TestController {
    @Resource
    TestService testService;

    @GetMapping("async-exception")
    public void testAsyncException() {
        testService.testAsyncException();
    }
}
