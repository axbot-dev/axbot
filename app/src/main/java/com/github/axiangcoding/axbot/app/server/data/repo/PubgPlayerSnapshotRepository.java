package com.github.axiangcoding.axbot.app.server.data.repo;

import com.github.axiangcoding.axbot.app.server.data.entity.PubgPlayerSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PubgPlayerSnapshotRepository extends JpaRepository<PubgPlayerSnapshot, Long> {
}