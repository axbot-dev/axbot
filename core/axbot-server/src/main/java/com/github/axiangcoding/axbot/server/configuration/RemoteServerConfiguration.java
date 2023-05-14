package com.github.axiangcoding.axbot.server.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axiangcoding.axbot.remote.bilibili.BiliClient;
import com.github.axiangcoding.axbot.remote.botmarket.BotMarketClient;
import com.github.axiangcoding.axbot.remote.cqhttp.CqHttpClient;
import com.github.axiangcoding.axbot.remote.kook.KookClient;
import com.github.axiangcoding.axbot.remote.qiniu.QiniuClient;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

import static com.theokanning.openai.service.OpenAiService.*;

@Configuration
public class RemoteServerConfiguration {
    @Resource
    BotConfProps botConfProps;

    @Bean
    public KookClient kookClient() {
        return new KookClient(botConfProps.getKook().getBotToken());
    }

    @Bean
    public BiliClient biliClient() {
        return new BiliClient();
    }

    @Bean
    public CqHttpClient cqHttpClient() {
        return new CqHttpClient(botConfProps.getCqhttp().getBaseUrl());
    }

    @Bean
    public QiniuClient qiniuClient() {
        BotConfProps.Censor censor = botConfProps.getCensor();
        return new QiniuClient(censor.getQiniu().getAccessToken(), censor.getQiniu().getSecretToken());
    }

    @Bean
    public OpenAiService openAiService() {
        BotConfProps.OpenAI openai = botConfProps.getOpenai();
        String apiKey = openai.getApiKey();
        String proxyType = openai.getProxy().getType();
        Proxy proxy;
        if (StringUtils.isBlank(proxyType) || "direct".equals(proxyType)) {
            proxy = Proxy.NO_PROXY;
        } else if ("http".equals(proxyType)) {
            String host = openai.getProxy().getHost();
            Integer port = openai.getProxy().getPort();
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        } else {
            proxy = Proxy.NO_PROXY;
        }

        ObjectMapper mapper = defaultObjectMapper();

        OkHttpClient client = defaultClient(apiKey, Duration.ofSeconds(20))
                .newBuilder()
                .proxy(proxy)
                .build();
        Retrofit retrofit = defaultRetrofit(client, mapper);
        OpenAiApi api = retrofit.create(OpenAiApi.class);
        return new OpenAiService(api);
    }

    /**
     * bot market的请求客户端
     *
     * @return
     */
    @Bean
    public BotMarketClient botMarketClient() {
        return new BotMarketClient();
    }
}
