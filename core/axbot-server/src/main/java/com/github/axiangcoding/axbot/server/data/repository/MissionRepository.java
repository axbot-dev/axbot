package com.github.axiangcoding.axbot.server.data.repository;

import com.github.axiangcoding.axbot.server.data.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional
public interface MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findByMissionId(UUID missionId);


    @Modifying
    @Query("update Mission m set m.status = ?1 where m.missionId = ?2")
    int updateStatusByMissionId(String status, @NonNull UUID missionId);

    @Modifying
    @Query("update Mission m set m.status = ?1, m.result = ?2 where m.missionId = ?3")
    int updateStatusAndResultByMissionId(String status, String result, UUID missionId);


}