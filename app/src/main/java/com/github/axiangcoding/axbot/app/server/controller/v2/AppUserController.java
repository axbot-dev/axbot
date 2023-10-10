package com.github.axiangcoding.axbot.app.server.controller.v2;

import com.github.axiangcoding.axbot.app.server.configuration.annotation.RequireLogin;
import com.github.axiangcoding.axbot.app.server.controller.entity.CommonError;
import com.github.axiangcoding.axbot.app.server.controller.entity.CommonResult;
import com.github.axiangcoding.axbot.app.server.controller.v2.entity.req.ExpireApiKeyBody;
import com.github.axiangcoding.axbot.app.server.controller.v2.entity.req.GenApiKeyBody;
import com.github.axiangcoding.axbot.app.server.controller.v2.entity.req.LoginBody;
import com.github.axiangcoding.axbot.app.server.controller.v2.entity.req.ResetPwdBody;
import com.github.axiangcoding.axbot.app.server.controller.v2.entity.resp.ApiKeyResp;
import com.github.axiangcoding.axbot.app.server.controller.v2.entity.resp.AppUserResp;
import com.github.axiangcoding.axbot.app.server.data.entity.ApiKey;
import com.github.axiangcoding.axbot.app.server.data.entity.AppUser;
import com.github.axiangcoding.axbot.app.server.service.ApiKeyService;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v2/user")
@Slf4j
public class AppUserController {
    @Resource
    private AppUserService appUserService;

    @Resource
    private ApiKeyService apiKeyService;

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

    @Operation(summary = "注册")
    @PostMapping("register")
    public CommonResult register() {
        return CommonResult.error(CommonError.NOT_SUPPORT, "self register is not support yet");
    }

    @RequireLogin
    @Operation(summary = "登出")
    @GetMapping("logout")
    public CommonResult logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return CommonResult.success("expire_session", true);
    }

    @RequireLogin
    @Operation(summary = "重置密码")
    @PostMapping("reset-password")
    public CommonResult resetPassword(HttpServletRequest request,
                                      @Valid @RequestBody ResetPwdBody body) {
        String newPassword = body.getNewPassword();
        String oldPassword = body.getOldPassword();
        if (Objects.equals(newPassword, oldPassword)) {
            return CommonResult.error(CommonError.INVALID_PARAM, "new password can't be the same as old password");
        }
        String userId = getUserIdFromSession(request);

        boolean updated = appUserService.updatePassword(userId, oldPassword, newPassword);
        if (updated) {
            request.getSession().invalidate();
            return CommonResult.success("changed", true);
        } else {
            return CommonResult.error(CommonError.UPDATE_PASSWORD_FAILED, "update password failed");
        }
    }

    @RequireLogin
    @Operation(summary = "获取当前用户信息")
    @GetMapping("me")
    public CommonResult me(HttpServletRequest request) {
        String userId = getUserIdFromSession(request);
        Optional<AppUser> opt = appUserService.findByUserId(userId);
        return opt.map(appUser -> CommonResult.success("user", AppUserResp.from(appUser)))
                .orElseGet(() -> CommonResult.error(CommonError.RESOURCE_NOT_EXIST));
    }

    @RequireLogin
    @Operation(summary = "获取当前用户的API Key列表")
    @GetMapping("api-key")
    public CommonResult apiKeys(HttpServletRequest request) {
        String userId = getUserIdFromSession(request);
        List<ApiKey> apiKeys = apiKeyService.findByUserId(userId);
        List<ApiKeyResp> apiKeyVos = apiKeys.stream().map(ApiKeyResp::from)
                .collect(Collectors.toList());
        return CommonResult.success("keys", apiKeyVos);
    }

    @RequireLogin
    @Operation(summary = "生成API Key")
    @PostMapping("api-key/generate")
    public CommonResult generateApiKey(HttpServletRequest request,
                                       @Valid @RequestBody GenApiKeyBody body) {
        String userId = getUserIdFromSession(request);
        String key = apiKeyService.generateKey(userId, body.getComment(), body.getNeverExpire(), body.getExpireInSecond());
        return CommonResult.success("api_key", key);
    }

    @RequireLogin
    @Operation(summary = "删除API Key")
    @PostMapping("api-key/expire")
    public CommonResult expireApiKey(@Valid @RequestBody ExpireApiKeyBody body) {
        boolean deleted = apiKeyService.expireKey(body.getApiKey());
        if (deleted) {
            return CommonResult.success();
        } else {
            return CommonResult.error(CommonError.ERROR, "expire api-key failed");
        }
    }

    private void setSession(HttpServletRequest request, AppUser user) {
        HttpSession session = request.getSession();
        session.setAttribute("loginTime", LocalDateTime.now());
        session.setAttribute("userId", user.getUserId());
        session.setMaxInactiveInterval(((int) Duration.ofMinutes(20).toSeconds()));
    }

    private String getUserIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        return userId.toString();
    }
}
