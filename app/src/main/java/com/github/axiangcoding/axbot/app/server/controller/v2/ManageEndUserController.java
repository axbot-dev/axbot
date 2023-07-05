package com.github.axiangcoding.axbot.app.server.controller.v2;

import com.github.axiangcoding.axbot.app.server.configuration.annotation.RequireApiKey;
import com.github.axiangcoding.axbot.app.server.controller.entity.CommonResult;
import com.github.axiangcoding.axbot.app.server.controller.v2.entity.req.SetEndUserSuperAdminBody;
import com.github.axiangcoding.axbot.app.server.service.EndUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manage/end-user")
public class ManageEndUserController {

    @Resource
    private EndUserService endUserService;

    @Operation(summary = "设置/取消平台用户管理员")
    @RequireApiKey(superAdmin = true)
    @PostMapping("super-admin")
    public CommonResult setSuperAdmin(@Valid @RequestBody SetEndUserSuperAdminBody body) {
        endUserService.setSuperAdmin(body.getPlatform(), body.getUserId(), body.getIsSuperAdmin());
        return CommonResult.success();
    }
}
