package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpNotificationOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookNotificationOutput;
import com.github.axiangcoding.axbot.remote.cqhttp.CqHttpClient;
import com.github.axiangcoding.axbot.remote.kook.KookClient;
import com.github.axiangcoding.axbot.remote.kook.entity.KookEvent;
import com.github.axiangcoding.axbot.remote.kook.service.entity.req.CreateDirectMsgReq;
import com.github.axiangcoding.axbot.remote.kook.service.entity.req.CreateMsgReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RemoteClientService {
    @Resource
    KookClient kookClient;

    @Resource
    CqHttpClient cqHttpClient;

    public void sendKookCardMsg(KookInteractiveOutput output) {
        CreateMsgReq req = new CreateMsgReq();
        req.setQuote(output.getMessageId());
        req.setType(KookEvent.TYPE_CARD);
        if (output.getTemp()) {
            req.setTempTargetId(output.getUserId());
        }
        req.setTargetId(output.getChannelId());
        req.setContent(output.getResponse());
        kookClient.createMessage(req);
    }

    public void sendKookCardMsg(KookNotificationOutput output) {
        CreateMsgReq req = new CreateMsgReq();
        req.setType(KookEvent.TYPE_CARD);
        req.setTargetId(output.getChannelId());
        req.setContent(output.getResponse());
        kookClient.createMessage(req);
    }

    public void sendKookPrivateCardMsg(String userId, String content) {
        CreateDirectMsgReq req = new CreateDirectMsgReq();
        req.setType(KookEvent.TYPE_CARD);
        req.setTargetId(userId);
        req.setContent(content);
        kookClient.createDirectMessage(req);
    }

    public void sendKookPrivateTextMsg(String userId, String content) {
        CreateDirectMsgReq req = new CreateDirectMsgReq();
        req.setType(KookEvent.TYPE_TEXT);
        req.setTargetId(userId);
        req.setContent(content);
        kookClient.createDirectMessage(req);
    }

    public void sendCqhttpMsg(CqhttpInteractiveOutput output) {
        String response = output.getResponse();
        String msg = "[CQ:reply,id=%s]%s".formatted(output.getMessageId(), response);
        cqHttpClient.sendGroupMsg(Long.valueOf(output.getGroupId()), msg, false);
    }

    public void sendCqhttpMsg(CqhttpNotificationOutput output) {
        cqHttpClient.sendGroupMsg(Long.valueOf(output.getGroupId()), output.getResponse(), false);
    }
}
