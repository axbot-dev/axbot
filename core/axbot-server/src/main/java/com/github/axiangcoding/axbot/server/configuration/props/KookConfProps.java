package com.github.axiangcoding.axbot.server.configuration.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "kook")
public class KookConfProps {
    Boolean enabled;
    String botToken;
    String verifyToken;
    List<String> triggerMessagePrefix;
}
