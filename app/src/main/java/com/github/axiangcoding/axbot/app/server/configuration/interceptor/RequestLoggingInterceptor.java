package com.github.axiangcoding.axbot.app.server.configuration.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Configuration
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        log.info("Begin to access URI: {}, Method: {}",
                request.getRequestURI(), request.getMethod()
        );
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                @NotNull HttpServletResponse response,
                                @NotNull Object handler, Exception ex) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("Response to URI: {}, Method: {}, cost: {} ms",
                request.getRequestURI(), request.getMethod(), duration
        );
    }
}
