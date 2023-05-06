package com.github.axiangcoding.axbot.server.configuration.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "bot")
@Configuration
public class BotConfProps {
    CqhttpConf cqhttp;
    KookConf kook;
    List<String> triggerMessagePrefix;
    Censor censor;

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

    @Data
    public static class Censor {
        Boolean enabled;
        Qiniu qiniu;

        @Data
        public static class Qiniu {
            String accessToken;
            String secretToken;
        }
    }
}
