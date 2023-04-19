package com.github.axiangcoding.axbot.server.controller.v1;

import com.github.axiangcoding.axbot.server.controller.entity.vo.req.QQWebhookEvent;
import com.github.axiangcoding.axbot.server.service.BotQQService;
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
@RequestMapping("v1/bot/qq")
@Slf4j
public class BotQQController {
    @Resource
    BotQQService botQQService;

    @PostMapping("cqhttp/webhook")
    public Map<String, Object> cqhttpWebhook(
            HttpServletRequest request,
            @RequestBody String body) {
        log.debug("receive cqhttp webhook msg, plain: {}", body);
        QQWebhookEvent event = JsonUtils.fromLowCaseUnderscoresJson(body, QQWebhookEvent.class);

        if (!botQQService.checkSecret(request.getHeader("X-Signature"), body)) {
            log.warn("no a valid cqhttp webhook message");
            return new HashMap<>();
        }

        return botQQService.DetermineMessageResponse(event);
    }
}
