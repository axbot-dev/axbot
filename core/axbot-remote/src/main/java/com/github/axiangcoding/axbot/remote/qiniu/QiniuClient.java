package com.github.axiangcoding.axbot.remote.qiniu;

import com.github.axiangcoding.axbot.remote.qiniu.service.entity.QiniuResponse;
import com.github.axiangcoding.axbot.remote.qiniu.service.entity.req.TextCensorReq;
import com.github.axiangcoding.axbot.remote.qiniu.service.entity.resp.TextCensorResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.List;

@Slf4j
public class QiniuClient {
    private final Client client;
    private final Auth auth;
    private static final Gson gson = new Gson();

    public QiniuClient(String accessKey, String secretKey) {
        if (StringUtils.isBlank(accessKey) || StringUtils.isBlank(secretKey)) {
            log.warn("qiniu accessKey or secretKey is blank, client will not work");
            this.auth = null;
            this.client = null;
            return;
        }
        this.auth = Auth.create(accessKey, secretKey);
        this.client = new Client();
    }

    public QiniuResponse<TextCensorResult> textCensor(String text) {
        try {
            String url = "https://ai.qiniuapi.com/v3/text/censor";
            TextCensorReq req = new TextCensorReq();
            req.getParams().setScenes(List.of("antispam"));
            req.getData().setText(text);

            String json = gson.toJson(req);
            String respJson = post(url, json.getBytes());
            Type listType = new TypeToken<QiniuResponse<TextCensorResult>>() {
            }.getType();
            return gson.fromJson(respJson, listType);
        } catch (QiniuException e) {
            log.warn("qiniu request error", e);
        }
        return new QiniuResponse<>();
    }

    private String post(String url, byte[] body) throws QiniuException {
        StringMap headers = auth.authorizationV2(url, "POST", body, Client.JsonMime);
        Response resp = client.post(url, body, headers, Client.JsonMime);
        return resp.bodyString();
    }
}
