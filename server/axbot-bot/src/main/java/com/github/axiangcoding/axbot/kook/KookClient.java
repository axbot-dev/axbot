package com.github.axiangcoding.axbot.kook;

public class KookClient {
    private static final String BASE_URL = "https://www.kookapp.cn/api";
    private static final String V3_API_URL = BASE_URL + "/v3";


    private final String botToken;

    public KookClient(String botToken) {
        this.botToken = botToken;
    }

}
