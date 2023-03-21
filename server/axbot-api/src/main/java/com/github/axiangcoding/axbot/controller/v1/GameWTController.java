package com.github.axiangcoding.axbot.controller.v1;

import com.github.axiangcoding.axbot.data.entity.Mission;
import com.github.axiangcoding.axbot.data.entity.WtGamerProfile;
import com.github.axiangcoding.axbot.entity.CommonError;
import com.github.axiangcoding.axbot.entity.CommonResult;
import com.github.axiangcoding.axbot.entity.vo.req.GetOrUpdateGamerProfile;
import com.github.axiangcoding.axbot.service.WTGameProfileService;
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
    WTGameProfileService wtGameProfileService;


    @GetMapping("gamer/profile")
    public CommonResult getGamerProfile(@Valid @ParameterObject GetOrUpdateGamerProfile obj) {
        Optional<WtGamerProfile> optProfile = wtGameProfileService.findByNickname(obj.getNickname());
        if (optProfile.isEmpty()) {
            return CommonResult.error(CommonError.RESOURCE_NOT_EXIST);
        }
        WtGamerProfile profile = optProfile.get();
        return CommonResult.success("profile", profile);
    }

    @PostMapping("gamer/profile/update")
    public CommonResult updateGamerProfile(@Valid @ParameterObject GetOrUpdateGamerProfile obj) {
        String nickname = obj.getNickname();
        if (!wtGameProfileService.canBeRefresh(nickname)) {
            return CommonResult.error(CommonError.WT_GAMER_PROFILE_REFRESH_TOO_OFTEN);
        }
        Mission mission = wtGameProfileService.submitMissionToUpdate(nickname);
        wtGameProfileService.putRefreshFlag(nickname);
        return CommonResult.success("missionId", mission.getMissionId());
    }

}
