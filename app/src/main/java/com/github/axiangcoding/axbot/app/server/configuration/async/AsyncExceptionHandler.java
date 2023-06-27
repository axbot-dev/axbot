package com.github.axiangcoding.axbot.app.server.configuration.async;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(@NotNull Throwable ex, Method method, @Nullable Object... params) {
        log.error("async task error, method: {}, params: {}", method.getName(), params, ex);
    }
}
