package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.engine.v1.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.v1.NotificationEvent;
import com.github.axiangcoding.axbot.engine.v1.SupportPlatform;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookNotificationInput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookEvent;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.CqhttpWebhookEvent;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.KookWebhookEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
@Service
public class WebhookService {
    @Resource
    BotConfProps botConfProps;

    @Resource
    BotService botService;

    /**
     * 校验kook回调的token
     *
     * @param retToken
     * @return
     */
    public boolean kookVerifyToken(String retToken) {
        return StringUtils.equals(retToken, botConfProps.getKook().getVerifyToken());
    }

    /**
     * 校验cqhttp回调的secret
     *
     * @param signatureHeader
     * @param body
     * @return
     */
    public boolean cqhttpCheckSecret(String signatureHeader, String body) {
        String secret = botConfProps.getCqhttp().getSecret();
        if (StringUtils.isBlank(secret)) {
            return true;
        }
        if (StringUtils.isBlank(signatureHeader)) {
            return false;
        }

        try {
            String algorithm = "HmacSHA1";
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(secretKeySpec);
            byte[] macBytes = mac.doFinal(body.getBytes());
            String macString = Hex.encodeHexString(macBytes);
            return StringUtils.equals(signatureHeader, "sha1=%s".formatted(macString));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("decode cqhttp x-signature failed.", e);
        }
        return false;
    }

    /**
     * 判断是否是触发机器人的消息
     *
     * @param message
     * @return
     */
    public boolean isTriggerMessage(String message) {
        if (StringUtils.isBlank(message)) {
            return false;
        }
        String[] contentSplit = StringUtils.split(message);
        List<String> prefixes = botConfProps.getTriggerMessagePrefix();
        boolean isTrigger = false;
        for (String prefix : prefixes) {
            if (prefix.equals(contentSplit[0])) {
                isTrigger = true;
                break;
            }
        }
        return isTrigger;
    }

    /**
     * 判断kook回调消息类型要进入哪个处理流程
     *
     * @param event
     * @return
     */
    public Map<String, Object> generateKookWebhookResp(KookWebhookEvent event) {
        HashMap<String, Object> map = new HashMap<>();
        KookEvent d = event.getD();
        if (Objects.equals(d.getType(), KookEvent.TYPE_TEXT) ||
                Objects.equals(d.getType(), KookEvent.TYPE_KMARKDOWN)) {
            String content = d.getContent();
            String[] contentSplit = StringUtils.split(content);
            String guildId = d.getExtra().getGuildId();
            String authorId = d.getAuthorId();
            String channelId = d.getTargetId();
            String msgId = d.getMsgId();
            if (isTriggerMessage(content)) {
                log.info("received trigger message from kook. user: [{}], message content: [{}]",
                        authorId, content);
                String command = StringUtils.join(Arrays.copyOfRange(contentSplit, 1, contentSplit.length), " ");
                KookInteractiveInput input = new KookInteractiveInput();
                input.setUserId(authorId);
                input.setGuildId(guildId);
                input.setChannelId(channelId);
                input.setMessageId(msgId);
                input.setCommand(InteractiveCommand.judgeCommand(command));
                input.setParamList(InteractiveCommand.getParamList(command));
                botService.responseForInteractiveAsync(SupportPlatform.KOOK, input);
            }
        } else if (Objects.equals(d.getType(), KookEvent.TYPE_SYSTEM_MESSAGE)) {
            String channelType = d.getChannelType();
            // 响应 webhook 验证
            if (Objects.equals(channelType, KookEvent.CHANNEL_TYPE_WEBHOOK_CHALLENGE)) {
                String challenge = d.getChallenge();
                map.put("challenge", challenge);
            } else if (Objects.equals(channelType, KookEvent.CHANNEL_TYPE_PERSON)) {
                String type = d.getExtra().getType();
                String guildId = (String) d.getExtra().getBody().get("guild_id");
                if ("self_joined_guild".equals(type)) {
                    KookNotificationInput input = new KookNotificationInput();
                    input.setEvent(NotificationEvent.JOIN_GUILD);
                    input.setGuildId(guildId);
                    botService.responseForNotificationAsync(SupportPlatform.KOOK, input);
                } else if ("self_exited_guild".equals(type)) {
                    KookNotificationInput input = new KookNotificationInput();
                    input.setEvent(NotificationEvent.EXIT_GUILD);
                    input.setGuildId(guildId);
                    botService.responseForNotificationAsync(SupportPlatform.KOOK, input);
                }
            }
        }
        return map;
    }

    /**
     * 判断cqhttp回调消息类型要进入哪个处理流程
     *
     * @param event
     * @return
     */
    public Map<String, Object> generateCqhttpWebhookResp(CqhttpWebhookEvent event) {
        String content = event.getRawMessage();
        Long userId = event.getUserId();
        Long groupId = event.getGroupId();
        Long messageId = event.getMessageId();
        String[] contentSplit = StringUtils.split(content);

        // 是群聊消息
        if ("group".equals(event.getMessageType()) && "normal".equals(event.getSubType())) {
            if (isTriggerMessage(content)) {
                log.info("received trigger message from cqhttp. user: [{}], message content: [{}]",
                        userId, content);
                String command = StringUtils.join(Arrays.copyOfRange(contentSplit, 1, contentSplit.length), " ");
                CqhttpInteractiveInput input = new CqhttpInteractiveInput();
                input.setUserId(String.valueOf(userId));
                input.setCommand(InteractiveCommand.judgeCommand(command));
                input.setParamList(InteractiveCommand.getParamList(command));
                input.setGroupId(String.valueOf(groupId));
                input.setMessageId(String.valueOf(messageId));
                botService.responseForInteractiveAsync(SupportPlatform.CQHTTP, input);
            }

        }
        return null;
    }

}
