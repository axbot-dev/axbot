package com.github.axiangcoding.axbot.server.controller.v1;

import com.github.axiangcoding.axbot.engine.v1.SupportPlatform;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.CqhttpWebhookEvent;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.KookWebhookEvent;
import com.github.axiangcoding.axbot.server.service.AxBotService;
import com.github.axiangcoding.axbot.server.service.WebhookService;
import com.github.axiangcoding.axbot.server.util.JsonUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("v1/bot")
@Slf4j
public class BotController {

    @Resource
    AxBotService axBotService;

    @Resource
    WebhookService webhookService;

    @PostMapping("kook/webhook")
    public Map<String, Object> KookWebhook(@RequestBody String body) {
        log.debug("receive kook webhook msg, plain: {}", body);
        if (!axBotService.isPlatformEnabled(SupportPlatform.PLATFORM_KOOK)) {
            log.info("platform kook not enabled, ignore webhook callback");
            return new HashMap<>();
        }

        KookWebhookEvent event = JsonUtils.fromLowCaseUnderscoresJson(body, KookWebhookEvent.class);
        if (!webhookService.kookVerifyToken(event.getD().getVerifyToken())) {
            log.warn("no a valid kook webhook message");
            return new HashMap<>();
        }
        return webhookService.generateKookWebhookResp(event);
    }

    @PostMapping("qq/cqhttp/webhook")
    public Map<String, Object> qqCqhttpWebhook(
            HttpServletRequest request,
            @RequestBody String body) {
        log.debug("receive cqhttp webhook msg, plain: {}", body);
        if (!axBotService.isPlatformEnabled(SupportPlatform.PLATFORM_CQHTTP)) {
            log.warn("platform cqhttp not enabled");
            return new HashMap<>();
        }

        CqhttpWebhookEvent event = JsonUtils.fromLowCaseUnderscoresJson(body, CqhttpWebhookEvent.class);

        if (!webhookService.cqhttpCheckSecret(request.getHeader("X-Signature"), body)) {
            log.warn("no a valid cqhttp webhook message");
            return new HashMap<>();
        }

        return webhookService.generateCqhttpWebhookResp(event);
    }
}
