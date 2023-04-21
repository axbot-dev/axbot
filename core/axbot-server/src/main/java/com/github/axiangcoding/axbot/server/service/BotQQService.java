package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.engine.UserInputCallback;
import com.github.axiangcoding.axbot.engine.entity.AxBotSupportPlatform;
import com.github.axiangcoding.axbot.engine.entity.AxBotUserOutput;
import com.github.axiangcoding.axbot.engine.entity.cqhttp.AxBotUserInputForCqhttp;
import com.github.axiangcoding.axbot.engine.entity.cqhttp.AxBotUserOutputForCqhttp;
import com.github.axiangcoding.axbot.remote.cqhttp.CqHttpClient;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.QQWebhookEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

@Service
@Slf4j
public class BotQQService {
    @Resource
    BotConfProps botConfProps;

    @Resource
    AxBotService axBotService;

    @Resource
    CqHttpClient cqHttpClient;

    @Resource
    UserInputRecordService userInputRecordService;


    public boolean checkSecret(String signatureHeader, String body) {
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

    public Map<String, Object> DetermineMessageResponse(QQWebhookEvent event) {
        String content = event.getRawMessage();
        Long userId = event.getUserId();
        Long groupId = event.getGroupId();
        Long messageId = event.getMessageId();
        String[] contentSplit = StringUtils.split(content);
        // 是群聊消息
        if ("group".equals(event.getMessageType()) && "normal".equals(event.getSubType())) {
            if (axBotService.isTriggerMessage(content)) {
                log.info("received trigger message from cqhttp. user: [{}], message content: [{}]",
                        userId, content);

                long inputId = userInputRecordService.saveRecordFromCqhttp(String.valueOf(userId), content, String.valueOf(groupId));
                String command = StringUtils.join(Arrays.copyOfRange(contentSplit, 1, contentSplit.length), " ");

                AxBotUserInputForCqhttp input = new AxBotUserInputForCqhttp();
                input.setFromUserId(String.valueOf(userId));
                input.setRequestCommand(command);
                input.setInputId(inputId);
                
                input.setFromMsgId(String.valueOf(messageId));
                input.setFromGroup(String.valueOf(groupId));

                axBotService.genResponseForInputAsync(AxBotSupportPlatform.PLATFORM_CQHTTP, input, new UserInputCallback() {
                    @Override
                    public void callback(AxBotUserOutput output) {
                        if (output == null) {
                            return;
                        }
                        AxBotUserOutputForCqhttp out = ((AxBotUserOutputForCqhttp) output);
                        String message = "[CQ:at,qq=%s] %s".formatted(output.getReplayToUser(), output.getContent());
                        cqHttpClient.sendGroupMsg(Long.valueOf(out.getToGroup()), message, false);
                    }

                    @Override
                    public void catchException(Exception e) {
                        String message = "[CQ:at,qq=%s] %s".formatted(input.getFromUserId(), "系统内部错误，请报告开发者");
                        cqHttpClient.sendGroupMsg(Long.valueOf(input.getFromGroup()), message, false);
                    }
                });
            }

        }
        return null;
    }

}
