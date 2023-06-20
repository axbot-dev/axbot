package com.github.axiangcoding.axbot.server.controller.v1;

import com.github.axiangcoding.axbot.server.configuration.annot.RequireApiKey;
import com.github.axiangcoding.axbot.server.controller.entity.CommonResult;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.SetSuperAdminReq;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.UnlockKookUserReq;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.UserCanUseAIReq;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/kook/manage")
public class KookManageController {
    @Resource
    KookUserSettingService kookUserSettingService;

    @RequireApiKey(admin = true)
    @GetMapping("blockedUsers")
    public CommonResult getBlockUsers() {
        List<KookUserSetting> users = kookUserSettingService.findAllByBanned(true);
        return CommonResult.success("users", users);
    }

    @RequireApiKey(admin = true)
    @PostMapping("user/unblock")
    public CommonResult unblockUser(@Valid @RequestBody UnlockKookUserReq req) {
        kookUserSettingService.unblockUser(req.getUserId());
        return CommonResult.success();
    }

    @RequireApiKey(admin = true)
    @PostMapping("user/canUseAI")
    public CommonResult setUserCanUseAI(@Valid @RequestBody UserCanUseAIReq req) {
        kookUserSettingService.setCanUseAI(req.getUserId(), req.getCanUseAI());
        return CommonResult.success();
    }

    @RequireApiKey(admin = true)
    @PostMapping("user/superAdmin")
    public CommonResult setSuperAdminUser(@Valid @RequestBody SetSuperAdminReq req) {
        kookUserSettingService.setSuperAdminUser(req.getUserId());
        return CommonResult.success();
    }
}
