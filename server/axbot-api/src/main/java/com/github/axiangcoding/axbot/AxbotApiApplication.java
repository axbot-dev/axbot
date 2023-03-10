package com.github.axiangcoding.axbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

@SpringBootApplication(nameGenerator = AnnotationBeanNameGenerator.class)
public class AxbotApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AxbotApiApplication.class, args);
    }

}
