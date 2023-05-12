package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.cqhttp.CqHttpClient;
import com.github.axiangcoding.axbot.remote.kook.KookClient;
import com.github.axiangcoding.axbot.remote.kook.entity.KookEvent;
import com.github.axiangcoding.axbot.remote.kook.service.entity.req.CreateMessageReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RemoteClientService {
    @Resource
    KookClient kookClient;

    @Resource
    CqHttpClient cqHttpClient;

    public void sendKookCardMsg(KookInteractiveOutput output) {
        CreateMessageReq req = new CreateMessageReq();
        req.setQuote(output.getMessageId());
        req.setType(KookEvent.TYPE_CARD);
        req.setTargetId(output.getChannelId());
        req.setContent(output.getResponse());
        kookClient.createMessage(req);
    }

    public void sendCqhttpMsg(CqhttpInteractiveOutput output) {
        cqHttpClient.sendGroupMsg(Long.valueOf(output.getGroupId()), output.getResponse(), false);
    }
}
