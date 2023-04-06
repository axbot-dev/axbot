package com.github.axiangcoding.axbot.server.controller.entity.vo.resp;

import com.github.axiangcoding.axbot.server.data.entity.Mission;
import com.google.gson.Gson;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MissionVo {

    String missionId;

    String type;
    String status;
    LocalDateTime createTime;
    LocalDateTime beginTime;
    LocalDateTime finishTime;
    Double process;
    Object result;

    public static MissionVo from(Mission mission) {
        MissionVo missionVo = new MissionVo();
        missionVo.setMissionId(mission.getMissionId().toString());
        missionVo.setType(mission.getType());
        missionVo.setStatus(mission.getStatus());
        missionVo.setCreateTime(mission.getCreateTime());
        missionVo.setBeginTime(mission.getBeginTime());
        missionVo.setFinishTime(mission.getFinishTime());
        missionVo.setProcess(mission.getProcess());
        missionVo.setResult(new Gson().fromJson(mission.getResult(), Object.class));
        return missionVo;
    }
}
