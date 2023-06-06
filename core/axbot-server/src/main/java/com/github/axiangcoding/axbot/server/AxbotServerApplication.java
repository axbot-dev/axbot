package com.github.axiangcoding.axbot.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

@SpringBootApplication(
        scanBasePackages = {"com.github.axiangcoding.axbot"},
        nameGenerator = AnnotationBeanNameGenerator.class,
        exclude = {SecurityAutoConfiguration.class})
public class AxbotServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AxbotServerApplication.class, args);
    }

}
