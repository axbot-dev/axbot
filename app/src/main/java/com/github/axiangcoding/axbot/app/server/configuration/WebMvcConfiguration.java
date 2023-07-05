package com.github.axiangcoding.axbot.app.server.configuration;


import com.github.axiangcoding.axbot.app.server.configuration.interceptor.AuthInterceptor;
import com.github.axiangcoding.axbot.app.server.configuration.interceptor.RequestLoggingInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Resource
    AuthInterceptor authInterceptor;

    @Resource
    RequestLoggingInterceptor requestLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor);
        registry.addInterceptor(authInterceptor);
    }
}
