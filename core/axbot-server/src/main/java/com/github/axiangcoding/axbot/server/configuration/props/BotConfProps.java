package com.github.axiangcoding.axbot.server.configuration.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "bot")
public class BotConfProps {
    CqhttpConf cqhttp;
    KookConf kook;
    List<String> triggerMessagePrefix;

    @Data
    public static class CqhttpConf {
        Boolean enabled;
        String baseUrl;
        String secret;
    }

    @Data
    public static class KookConf {
        Boolean enabled;
        String botToken;
        String verifyToken;
    }
}
