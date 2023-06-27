package com.github.axiangcoding.axbot.app.server.controller.v1;

import com.github.axiangcoding.axbot.app.server.controller.entity.CommonError;
import com.github.axiangcoding.axbot.app.server.controller.entity.CommonResult;
import com.github.axiangcoding.axbot.app.server.controller.v1.entity.req.LoginBody;
import com.github.axiangcoding.axbot.app.server.controller.v1.entity.req.ResetPwdBody;
import com.github.axiangcoding.axbot.app.server.data.entity.AppUser;
import com.github.axiangcoding.axbot.app.server.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * TODO 填充逻辑
 */
@RestController
@RequestMapping("v1/user")
@Slf4j
public class AppUserController {
    @Resource
    AppUserService appUserService;

    @Operation(summary = "登录")
    @PostMapping("login")
    public CommonResult login(
            HttpServletRequest request,
            @Valid @RequestBody LoginBody body
    ) {
        String username = body.getUsername();
        String password = body.getPassword();
        boolean checkPwd = appUserService.checkPassword(username, password);
        if (!checkPwd) {
            return CommonResult.error(CommonError.LOGIN_FAILED);
        }

        Optional<AppUser> opt = appUserService.findByUsername(username);
        if (opt.isEmpty()) {
            return CommonResult.error(CommonError.LOGIN_FAILED);
        }
        AppUser user = opt.get();
        if (!AppUser.STATUS.NORMAL.name().equals(user.getStatus())) {
            return CommonResult.error(CommonError.LOGIN_FAILED, "your account is locked");
        }
        setSession(request, user);
        log.info("user [{}] login success", username);
        return CommonResult.success("grant_session", true);
    }

    @Operation(summary = "登出")
    @GetMapping("logout")
    public CommonResult logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return CommonResult.success("expire_session", true);
    }

    @Operation(summary = "注册")
    @PostMapping("register")
    public CommonResult register() {
        return CommonResult.error(CommonError.NOT_SUPPORT, "self register is not support yet");
    }

    @Operation(summary = "重置密码")
    @PostMapping("reset-password")
    public CommonResult resetPassword(HttpServletRequest request,
                                      @Valid @RequestBody ResetPwdBody body) {
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

    private void setSession(HttpServletRequest request, AppUser user) {
        HttpSession session = request.getSession();
        session.setAttribute("loginTime", LocalDateTime.now());
        session.setAttribute("userId", user.getUserId());
        session.setMaxInactiveInterval(((int) Duration.ofMinutes(5).toSeconds()));
    }

    private Optional<String> getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return Optional.empty();
        }
        return Optional.of(userId.toString());
    }
}
