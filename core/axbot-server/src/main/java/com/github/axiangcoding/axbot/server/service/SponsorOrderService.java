package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.engine.entity.AxBotSupportPlatform;
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

    public Optional<SponsorOrder> findByOrderId(String orderId) {
        return sponsorOrderRepository.findByOrderId(UUID.fromString(orderId));
    }

    public String generatePersonalOrder(AxBotSupportPlatform platform, String guildId, String channelId, String userId) {
        SponsorOrder entity = new SponsorOrder();
        entity.setPlatform(platform.getName());
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

    public void activeOrder(String orderId, String planName, int month) {
        Optional<SponsorOrder> opt = findByOrderId(orderId);
        if (opt.isEmpty()) {
            log.warn("order [{}] is not exist", orderId);
            return;
        }
        SponsorOrder entity = opt.get();
        entity.setMonth(month);
        entity.setPlanName(planName);
        entity.setStatus(SponsorOrder.STATUS.SUCCESS.getName());
        sponsorOrderRepository.save(entity);
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
}
