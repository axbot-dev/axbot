package com.github.axiangcoding.axbot.server.controller.v1;

import com.github.axiangcoding.axbot.server.configuration.annot.RequireApiKey;
import com.github.axiangcoding.axbot.server.data.entity.Mission;
import com.github.axiangcoding.axbot.server.controller.entity.CommonError;
import com.github.axiangcoding.axbot.server.controller.entity.CommonResult;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.GetMissionReq;
import com.github.axiangcoding.axbot.server.controller.entity.vo.resp.MissionVo;
import com.github.axiangcoding.axbot.server.service.MissionService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("v1/mission")
public class MissionController {
    @Resource
    MissionService missionService;

    @RequireApiKey
    @GetMapping
    public CommonResult getMission(@Valid @ParameterObject GetMissionReq req) {
        Optional<Mission> optMission = missionService.findByMissionId(req.getId());
        if (optMission.isEmpty()) {
            return CommonResult.error(CommonError.RESOURCE_NOT_EXIST);
        }
        Mission mission = optMission.get();
        return CommonResult.success("mission", MissionVo.from(mission));
    }

}
