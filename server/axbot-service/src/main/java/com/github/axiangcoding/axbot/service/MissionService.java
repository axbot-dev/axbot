package com.github.axiangcoding.axbot.service;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.data.entity.Mission;
import com.github.axiangcoding.axbot.data.repository.MissionRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class MissionService {
    @Resource
    MissionRepository missionRepository;

    public Mission save(Mission mission) {
        return missionRepository.save(mission);
    }

    public Optional<Mission> findByMissionId(String missionId) {
        UUID uuid = UUID.fromString(missionId);
        return missionRepository.findByMissionId(uuid);
    }

    public void setPending(String missionId) {
        Optional<Mission> optM = findByMissionId(missionId);
        if (optM.isEmpty()) {
            return;
        }
        Mission mission = optM.get();
        mission.setStatus(Mission.STATUS_PENDING);
        mission.setProcess(0.0);
        missionRepository.save(mission);
    }

    public void setRunning(String missionId, Double process) {
        Optional<Mission> optM = findByMissionId(missionId);
        if (optM.isEmpty()) {
            return;
        }
        Mission mission = optM.get();
        mission.setStatus(Mission.STATUS_RUNNING);
        mission.setProcess(process);
        if (mission.getBeginTime() == null) {
            mission.setBeginTime(LocalDateTime.now());
        }
        missionRepository.save(mission);
    }

    public void setSuccess(String missionId, JSONObject result) {
        Optional<Mission> optM = findByMissionId(missionId);
        if (optM.isEmpty()) {
            return;
        }
        Mission mission = optM.get();
        mission.setStatus(Mission.STATUS_SUCCESS);
        mission.setProcess(100.0);
        mission.setFinishTime(LocalDateTime.now());
        mission.setResult(result.toJSONString());
        missionRepository.save(mission);
    }

    public void setFailed(String missionId, JSONObject result) {
        Optional<Mission> optM = findByMissionId(missionId);
        if (optM.isEmpty()) {
            return;
        }
        Mission mission = optM.get();
        mission.setStatus(Mission.STATUS_FAILED);
        mission.setProcess(100.0);
        mission.setFinishTime(LocalDateTime.now());
        mission.setResult(result.toJSONString());
        missionRepository.save(mission);
    }

    public void setFailed(String missionId, Exception e) {
        JSONObject result = new JSONObject();
        String message = e.getMessage();
        result.put("error", message);
        setFailed(missionId, result);
    }
}
