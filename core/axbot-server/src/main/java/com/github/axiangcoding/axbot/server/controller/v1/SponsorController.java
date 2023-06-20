package com.github.axiangcoding.axbot.server.controller.v1;

import com.github.axiangcoding.axbot.server.controller.entity.vo.req.AFDianWebhook;
import com.github.axiangcoding.axbot.server.service.SponsorOrderService;
import com.github.axiangcoding.axbot.server.util.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("v1/sponsor")
@Slf4j
public class SponsorController {
    @Resource
    SponsorOrderService sponsorOrderService;


    @PostMapping("/afdian/webhook")
    public Map<String, Object> afdianWebhook(@RequestBody String body) {
        AFDianWebhook afDianWebhook = JsonUtils.fromLowCaseUnderscoresJson(body, AFDianWebhook.class);
        if (afDianWebhook.getEc() != 200) {
            return null;
        }
        AFDianWebhook.IData.Order order = afDianWebhook.getData().getOrder();

        String customOrderId = order.getCustomOrderId();
        String tradeNo = order.getOutTradeNo();
        Integer month = order.getMonth();
        String planTitle = order.getPlanTitle();
        if (sponsorOrderService.checkOrderFromAfdian(tradeNo)) {
            sponsorOrderService.activeOrder(tradeNo, customOrderId, planTitle, month);
        } else {
            log.warn("not a valid order! customOrderId: {}, planTitle: {}, month: {}", customOrderId, planTitle, month);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("ec", 200);
        return map;
    }
}
