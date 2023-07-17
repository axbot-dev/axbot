package com.github.axiangcoding.axbot.app.server.configuration.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "app")
@Configuration
public class AppConfProps {
    BotMarketConf botMarket;
    SchedulingConf scheduling;
    OpenAI openai;
    Afdian afdian;
    Censor censor;

    @Data
    public static class BotMarketConf {
        /**
         * 设置bot market在线的uuid
         */
        String uuid;
    }

    @Data
    public static class SchedulingConf {
        Boolean enabled;
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

}
