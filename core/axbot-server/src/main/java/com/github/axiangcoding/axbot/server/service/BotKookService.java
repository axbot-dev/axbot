package com.github.axiangcoding.axbot.server.service;


import com.github.axiangcoding.axbot.bot.kook.KookClient;
import com.github.axiangcoding.axbot.bot.kook.entity.KookEvent;
import com.github.axiangcoding.axbot.bot.kook.service.entity.CreateMessageReq;
import com.github.axiangcoding.axbot.engine.AxBotEngine;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotInputForKook;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotOutputForKook;
import com.github.axiangcoding.axbot.server.configuration.props.KookConfProps;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.KookWebhookEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class BotKookService {
    @Resource
    KookClient kookClient;

    @Resource
    KookConfProps kookConfProps;

    @Resource
    AxBotEngine axBotEngine;


    public boolean compareVerifyToken(String retToken) {
        return StringUtils.equals(retToken, kookConfProps.getVerifyToken());
    }

    /**
     * 判断回调消息类型要进入哪个处理流程
     *
     * @param event
     * @return
     */
    public Map<String, Object> DetermineMessageResponse(KookWebhookEvent event) {
        HashMap<String, Object> map = new HashMap<>();
        KookEvent d = event.getD();
        if (Objects.equals(d.getType(), KookEvent.TYPE_TEXT) ||
                Objects.equals(d.getType(), KookEvent.TYPE_KMARKDOWN)) {
            String content = d.getContent();
            String command = null;
            List<String> prefixes = kookConfProps.getTriggerMessagePrefix();
            boolean isTrigger = false;
            for (String prefix : prefixes) {
                if (StringUtils.startsWith(StringUtils.trim(content), prefix)) {
                    isTrigger = true;
                    command = RegExUtils.replaceAll(StringUtils.trim(content), prefix, "");
                    break;
                }
            }
            if (isTrigger) {
                AxBotInputForKook input = new AxBotInputForKook();
                input.setRequestUser(d.getAuthorId());
                input.setRequestCommand(command);
                input.setRequestMsgId(d.getMsgId());
                input.setRequestChannel(d.getTargetId());
                axBotEngine.generateResponseAsync(AxBotEngine.PLATFORM_KOOK, input, output -> {
                    AxBotOutputForKook out = ((AxBotOutputForKook) output);
                    CreateMessageReq req = new CreateMessageReq();
                    req.setType(KookEvent.TYPE_KMARKDOWN);
                    req.setQuote(out.getReplayToMsg());
                    req.setTargetId(out.getReplayToChannel());
                    req.setContent(out.getContent());
                    kookClient.createMessage(req);
                });
            }


        } else if (Objects.equals(d.getType(), KookEvent.TYPE_MESSAGE)) {
            if (Objects.equals(d.getChannelType(), KookEvent.CHANNEL_TYPE_WEBHOOK_CHALLENGE)) {
                String challenge = d.getChallenge();
                map.put("challenge", challenge);
            }
        } else {

        }
        return map;
    }

    private void processByAxBot() {

    }
}
