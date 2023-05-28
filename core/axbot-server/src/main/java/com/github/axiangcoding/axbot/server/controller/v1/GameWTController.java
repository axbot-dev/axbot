package com.github.axiangcoding.axbot.server.controller.v1;

import com.github.axiangcoding.axbot.server.configuration.annot.RequireApiKey;
import com.github.axiangcoding.axbot.server.data.entity.Mission;
import com.github.axiangcoding.axbot.server.data.entity.WtGamerProfile;
import com.github.axiangcoding.axbot.server.controller.entity.CommonError;
import com.github.axiangcoding.axbot.server.controller.entity.CommonResult;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.GamerProfileReq;
import com.github.axiangcoding.axbot.server.service.WTGamerProfileService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("v1/game/warthunder")
public class GameWTController {
    @Resource
    WTGamerProfileService wtGamerProfileService;

    @RequireApiKey
    @GetMapping("gamer/profile")
    public CommonResult getGamerProfile(@Valid @ParameterObject GamerProfileReq obj) {
        Optional<WtGamerProfile> optProfile = wtGamerProfileService.findByNickname(obj.getNickname());
        if (optProfile.isEmpty()) {
            return CommonResult.error(CommonError.RESOURCE_NOT_EXIST);
        }
        WtGamerProfile profile = optProfile.get();
        return CommonResult.success("profile", profile);
    }

    @RequireApiKey
    @PostMapping("gamer/profile/update")
    public CommonResult updateGamerProfile(@Valid @ParameterObject GamerProfileReq obj) {
        String nickname = obj.getNickname();
        if (!wtGamerProfileService.canBeRefresh(nickname)) {
            return CommonResult.error(CommonError.WT_GAMER_PROFILE_REFRESH_TOO_OFTEN);
        }
        Mission mission = wtGamerProfileService.submitMissionToUpdate(nickname);
        return CommonResult.success("missionId", mission.getMissionId());
    }

    @RequireApiKey(admin = true)
    @PostMapping("gamer/profile/update/lock/reset")
    public CommonResult resetUpdateLock(@Valid @ParameterObject GamerProfileReq obj) {
        wtGamerProfileService.deleteRefreshFlag(obj.getNickname());
        return CommonResult.success();
    }

}
