package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.engine.UserInputCallback;
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
import java.util.List;
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
        String[] contentSplit = StringUtils.split(content);

        if ("group".equals(event.getMessageType()) && "normal".equals(event.getSubType())) {

            String command = "";
            List<String> prefixes = botConfProps.getTriggerMessagePrefix();
            boolean isTrigger = false;
            for (String prefix : prefixes) {
                if (prefix.equals(contentSplit[0])) {
                    isTrigger = true;
                    command = StringUtils.join(Arrays.copyOfRange(contentSplit, 1, contentSplit.length), " ");
                    break;
                }
            }
            if (isTrigger) {
                log.info("received trigger message from kook. user: [{}], message content: [{}]",
                        event.getUserId(), content);
                // String guildId = d.getExtra().getGuildId();
                // Optional<KookGuildSetting> optKgs = kookGuildSettingService.findBytGuildId(guildId);
                // if (optKgs.isEmpty()) {
                //     kookGuildSettingService.updateWhenJoin(guildId);
                // } else {
                //     // TODO 增加使用量
                // }
                AxBotUserInputForCqhttp input = new AxBotUserInputForCqhttp();
                input.setFromUserId(String.valueOf(event.getUserId()));
                input.setRequestCommand(command);
                input.setFromMsgId(String.valueOf(event.getMessageId()));
                input.setFromGroup(String.valueOf(event.getGroupId()));

                axBotService.genResponseForInputAsync(AxBotService.PLATFORM_CQHTTP, input, new UserInputCallback() {
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
