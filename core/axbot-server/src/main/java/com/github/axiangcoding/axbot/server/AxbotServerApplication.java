package com.github.axiangcoding.axbot.server;

import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

@SpringBootApplication(nameGenerator = AnnotationBeanNameGenerator.class, exclude = {SecurityAutoConfiguration.class})
@EnableConfigurationProperties(BotConfProps.class)
@Slf4j
public class AxbotServerApplication {

    public static void init() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> log.error("thread throw uncaught exception", e));
    }

    public static void main(String[] args) {
        init();
        SpringApplication.run(AxbotServerApplication.class, args);
    }

}
