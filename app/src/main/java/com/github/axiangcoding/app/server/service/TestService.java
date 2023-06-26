package com.github.axiangcoding.app.server.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Async
    public void testAsyncException() {
        throw new RuntimeException("test async exception");
    }
}
