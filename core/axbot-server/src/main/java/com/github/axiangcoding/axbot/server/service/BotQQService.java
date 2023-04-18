package com.github.axiangcoding.axbot.server.service;

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
import java.util.Map;

@Service
@Slf4j
public class BotQQService {
    @Resource
    BotConfProps botConfProps;

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
        return null;
    }
}
