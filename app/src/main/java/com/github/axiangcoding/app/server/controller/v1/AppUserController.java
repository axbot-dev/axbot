package com.github.axiangcoding.app.server.controller.v1;

import com.github.axiangcoding.app.server.controller.entity.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO 填充逻辑
 */
@RestController
@RequestMapping("v1/app-user")
@Slf4j
public class AppUserController {
    @Operation(summary = "登录")
    @PostMapping("login")
    public CommonResult login() {
        return CommonResult.success();
    }

    @Operation(summary = "登出")
    @GetMapping("logout")
    public CommonResult logout() {
        return CommonResult.success();
    }

    @Operation(summary = "注册")
    @PostMapping("register")
    public CommonResult register() {
        return CommonResult.success();
    }

    @Operation(summary = "重置密码")
    @PostMapping("reset-password")
    public CommonResult resetPassword() {
        return CommonResult.success();
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("me")
    public CommonResult me() {
        return CommonResult.success();
    }

    @Operation(summary = "获取当前用户的API Key列表")
    @GetMapping("api-keys")
    public CommonResult apiKeys() {
        return CommonResult.success();
    }

    @Operation(summary = "生成API Key")
    @PostMapping("api-key/generate")
    public CommonResult generateApiKey() {
        return CommonResult.success();
    }

    @Operation(summary = "删除API Key")
    @PostMapping("api-key/expire")
    public CommonResult expireApiKey() {
        return CommonResult.success();
    }
}
