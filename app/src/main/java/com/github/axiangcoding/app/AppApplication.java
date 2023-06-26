package com.github.axiangcoding.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

@SpringBootApplication(
        // nameGenerator = AnnotationBeanNameGenerator.class,
        exclude = {SecurityAutoConfiguration.class})
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

}
