package com.github.axiangcoding.axbot.configuration.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "kook")
public class KookConfProps {
    private String botToken;
}
