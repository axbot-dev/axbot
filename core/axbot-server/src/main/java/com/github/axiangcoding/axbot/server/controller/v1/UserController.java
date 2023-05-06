package com.github.axiangcoding.axbot.server.controller.v1;

import com.github.axiangcoding.axbot.server.configuration.annot.RequireLogin;
import com.github.axiangcoding.axbot.server.controller.entity.CommonError;
import com.github.axiangcoding.axbot.server.controller.entity.CommonResult;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.GenApiKeyReq;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.LoginReq;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.RegisterReq;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.UpdatePwdReq;
import com.github.axiangcoding.axbot.server.controller.entity.vo.resp.GlobalUserVo;
import com.github.axiangcoding.axbot.server.data.entity.GlobalUser;
import com.github.axiangcoding.axbot.server.service.ApiKeyService;
import com.github.axiangcoding.axbot.server.service.GlobalUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("v1/user")
public class UserController {
    @Resource
    GlobalUserService globalUserService;

    @Resource
    ApiKeyService apiKeyService;

    @PostMapping("login")
    public CommonResult login(HttpServletRequest request, @Valid @RequestBody LoginReq req) {
        String username = req.getUsername();
        boolean checkPwd = globalUserService.checkPwd(username, req.getPassword());
        if (!checkPwd) {
            return CommonResult.error(CommonError.LOGIN_FAILED);
        }

        Optional<GlobalUser> optGu = globalUserService.findByUsername(username);
        if (optGu.isEmpty()) {
            return CommonResult.error(CommonError.LOGIN_FAILED);
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginTime", LocalDateTime.now());
        session.setAttribute("userId", optGu.get().getUserId().toString());
        session.setMaxInactiveInterval(((int) Duration.ofMinutes(5).toSeconds()));

        return CommonResult.success();
    }

    @RequireLogin
    @GetMapping("logout")
    public CommonResult logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return CommonResult.success();
    }

    @PostMapping("register")
    public CommonResult register(@Valid @RequestBody RegisterReq req) {
        String username = req.getUsername();
        Optional<GlobalUser> opt = globalUserService.findByUsername(username);
        if (opt.isPresent()) {
            return CommonResult.error(CommonError.REGISTER_FAILED, "username exist");
        }

        GlobalUser user = globalUserService.registerUser(username, req.getPassword());
        return CommonResult.success("user", GlobalUserVo.from(user));
    }

    @RequireLogin
    @PostMapping("password/update")
    public CommonResult updatePassword(HttpServletRequest request, @Valid @RequestBody UpdatePwdReq req) {
        if (Objects.equals(req.getNewPassword(), req.getOldPassword())) {
            return CommonResult.error(CommonError.INVALID_PARAM, "new password can't be the same as old password");
        }

        String userId = globalUserService.getUserIdFromRequest(request);
        boolean updated = globalUserService.updatePassword(userId, req.getOldPassword(), req.getNewPassword());
        if (updated) {
            request.getSession().invalidate();
            return CommonResult.success();
        } else {
            return CommonResult.error(CommonError.UPDATE_PASSWORD_FAILED);
        }
    }

    @RequireLogin
    @GetMapping("me")
    public CommonResult getMe(HttpServletRequest request) {
        String userId = globalUserService.getUserIdFromRequest(request);
        Optional<GlobalUser> opt = globalUserService.findByUserId(userId);
        if (opt.isEmpty()) {
            return CommonResult.error(CommonError.RESOURCE_NOT_EXIST);
        }
        return CommonResult.success("user", GlobalUserVo.from(opt.get()));
    }

    @PostMapping("invite")
    public CommonResult invite() {
        return CommonResult.error(CommonError.NOT_SUPPORT);
    }

    @RequireLogin
    @PostMapping("apikey/generate")
    public CommonResult generateApiKey(HttpServletRequest request, @Valid @RequestBody GenApiKeyReq req) {
        String userId = globalUserService.getUserIdFromRequest(request);
        String key = apiKeyService.generateApiKey(userId, req.getComment(), req.getNeverExpire(), req.getExpireInSecond());
        return CommonResult.success("apiKey", key);
    }

    @RequireLogin
    @PostMapping("apikey/expire")
    public CommonResult expireApiKey(HttpServletRequest request) {
        String apiKey = apiKeyService.getApiKeyFromRequest(request);
        boolean deleted = apiKeyService.deleteByKey(apiKey);
        if (deleted) {
            return CommonResult.success();
        } else {
            return CommonResult.error(CommonError.ERROR, "expire api-key failed");
        }
    }


}
