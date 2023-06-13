package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.engine.SupportPlatform;
import com.github.axiangcoding.axbot.remote.afdian.AfdianClient;
import com.github.axiangcoding.axbot.remote.afdian.service.entity.AfdianResponse;
import com.github.axiangcoding.axbot.remote.afdian.service.entity.resp.QueryOrderResp;
import com.github.axiangcoding.axbot.server.data.entity.SponsorOrder;
import com.github.axiangcoding.axbot.server.data.repository.SponsorOrderRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class SponsorOrderService {
    @Resource
    SponsorOrderRepository sponsorOrderRepository;

    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    AfdianClient afdianClient;

    public Optional<SponsorOrder> findByOrderId(String orderId) {
        return sponsorOrderRepository.findByOrderId(UUID.fromString(orderId));
    }

    public String generatePersonalOrder(SupportPlatform platform, String guildId, String channelId, String userId) {
        SponsorOrder entity = new SponsorOrder();
        entity.setPlatform(platform.getLabel());
        entity.setFromUserId(userId);
        entity.setFromGuildId(guildId);
        entity.setFromChannelId(channelId);
        entity.setPlan(SponsorOrder.PLAN.BASIC_PERSONAL.getName());
        entity.setStatus(SponsorOrder.STATUS.PENDING.getName());
        UUID orderId = UUID.randomUUID();
        entity.setOrderId(orderId);
        sponsorOrderRepository.save(entity);
        return orderId.toString();
    }

    public void activeOrder(String tradeNo, String orderId, String planName, int month) {
        Optional<SponsorOrder> opt = findByOrderId(orderId);
        if (opt.isEmpty()) {
            log.warn("order [{}] is not exist", orderId);
            return;
        }
        SponsorOrder entity = opt.get();
        if (!SponsorOrder.STATUS.PENDING.getName().equals(entity.getStatus())) {
            log.info("order [{}] is not pending, active failed", orderId);
            return;
        }
        entity.setMonth(month);
        entity.setPlanTitle(planName);
        entity.setTradeNo(tradeNo);
        entity.setStatus(SponsorOrder.STATUS.SUCCESS.getName());
        sponsorOrderRepository.save(entity);

        String platform = entity.getPlatform();
        if (SupportPlatform.KOOK.getLabel().equals(platform)) {
            kookUserSettingService.updateSubscribe(entity.getFromUserId(), entity.getPlan(), month);
        }
    }

    public void closeOrder(String orderId) {
        Optional<SponsorOrder> opt = findByOrderId(orderId);
        if (opt.isEmpty()) {
            return;
        }
        SponsorOrder entity = opt.get();
        entity.setStatus(SponsorOrder.STATUS.CLOSE.getName());
        sponsorOrderRepository.save(entity);
    }

    public boolean checkOrderFromAfdian(String tradeNo) {
        AfdianResponse<QueryOrderResp> resp = afdianClient.queryOrder(null, tradeNo);
        QueryOrderResp data = resp.getData();
        if (data.getList() == null || data.getList().isEmpty()) {
            return false;
        }
        QueryOrderResp.OrderList order = data.getList().get(0);
        String tn = order.getOutTradeNo();
        return tradeNo.equals(tn);
    }

}
