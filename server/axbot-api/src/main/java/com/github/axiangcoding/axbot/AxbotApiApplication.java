package com.github.axiangcoding.axbot;

import com.github.axiangcoding.axbot.configuration.props.KookConfProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

@SpringBootApplication(nameGenerator = AnnotationBeanNameGenerator.class)
@EnableConfigurationProperties(KookConfProps.class)
public class AxbotApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AxbotApiApplication.class, args);
    }

}
