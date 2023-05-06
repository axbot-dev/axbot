package com.github.axiangcoding.axbot.server.controller.v1;

import com.github.axiangcoding.axbot.server.configuration.annot.RequireApiKey;
import com.github.axiangcoding.axbot.server.controller.entity.CommonResult;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.UnlockKookUserReq;
import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import com.github.axiangcoding.axbot.server.data.entity.KookUserSetting;
import com.github.axiangcoding.axbot.server.service.KookGuildSettingService;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/kook/manage")
public class KookManageController {
    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    KookGuildSettingService kookGuildSettingService;

    @RequireApiKey(admin = true)
    @GetMapping("users/ban")
    public CommonResult getBanUsers() {
        List<KookUserSetting> users = kookUserSettingService.findAllByBanned(true);
        return CommonResult.success("users", users);
    }

    @RequireApiKey(admin = true)
    @PostMapping("users/unblock")
    public CommonResult unblockUser(@Valid @ParameterObject UnlockKookUserReq req) {
        kookUserSettingService.unblockUser(req.getUserId());
        return CommonResult.success();
    }
}
