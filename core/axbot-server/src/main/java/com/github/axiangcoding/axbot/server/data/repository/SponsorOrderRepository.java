package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.SponsorOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SponsorOrderRepository extends JpaRepository<SponsorOrder, Long> {
    Optional<SponsorOrder> findByOrderId(UUID orderId);
}