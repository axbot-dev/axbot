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
    BotMarketConf botMarket;
    KookConf kook;
    OpenAI openai;
    Afdian afdian;

    /**
     * 触发机器人命令的前缀
     */
    List<String> triggerMessagePrefix;
    Censor censor;

    @Data
    public static class CqhttpConf {
        /**
         * 是否启动cqhttp机器人
         */
        Boolean enabled;
        /**
         * cqhttp的url
         */
        String baseUrl;
        /**
         * cqhttp的secret
         */
        String secret;
    }

    @Data
    public static class KookConf {
        /**
         * 是否启动kook机器人
         */
        Boolean enabled;
        /**
         * 机器人token
         */
        String botToken;
        /**
         * 机器人的认证token
         */
        String verifyToken;
    }

    @Data
    public static class BotMarketConf {
        /**
         * 设置bot market在线的uuid
         */
        String uuid;
    }

    @Data
    public static class Censor {
        /**
         * 是否启动文本审核
         */
        Boolean enabled;
        Qiniu qiniu;

        @Data
        public static class Qiniu {
            /**
             * 七牛云的accessToken
             */
            String accessToken;
            /**
             * 七牛云的secretToken
             */
            String secretToken;
        }
    }

    @Data
    public static class OpenAI {
        /**
         * openai的api key
         */
        String apiKey;
        Proxy proxy;

        @Data
        public static class Proxy {
            /**
             * 代理方式。目前仅支持direct和http
             */
            String type;
            /**
             * 如果使用http形式的代理，这里填写的是代理的host
             */
            String host;
            /**
             * 如果使用http形式的代理，这里填写的是代理的port
             */
            Integer port;
        }

    }

    @Data
    public static class Afdian {
        /**
         * 爱发电的用户id
         */
        String userId;
        /**
         * 爱发电的token
         */
        String token;
    }

    public String getDefaultTriggerPrefix() {
        return triggerMessagePrefix.get(0);
    }
}
