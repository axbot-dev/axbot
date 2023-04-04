package com.github.axiangcoding.axbot.server.controller.v1;

import com.github.axiangcoding.axbot.server.configuration.annot.RequireLogin;
import com.github.axiangcoding.axbot.server.configuration.exception.BusinessException;
import com.github.axiangcoding.axbot.server.controller.entity.CommonError;
import com.github.axiangcoding.axbot.server.controller.entity.CommonResult;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.LoginReq;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.RegisterReq;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.UpdatePwdReq;
import com.github.axiangcoding.axbot.server.data.entity.GlobalUser;
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
        return CommonResult.success("user", user.toDislayMap());
    }

    @RequireLogin
    @PostMapping("password/update")
    public CommonResult updatePassword(HttpServletRequest request, @Valid @RequestBody UpdatePwdReq req) {
        if (Objects.equals(req.getNewPassword(), req.getOldPassword())) {
            return CommonResult.error(CommonError.INVALID_PARAM, "new password can't be the same as old password");
        }

        String userId = getUserIdFromRequest(request);
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
        String userId = getUserIdFromRequest(request);
        Optional<GlobalUser> opt = globalUserService.findByUserId(userId);
        if (opt.isEmpty()) {
            return CommonResult.error(CommonError.RESOURCE_NOT_EXIST);
        }
        return CommonResult.success("user", opt.get().toDislayMap());
    }

    @PostMapping("invite")
    public CommonResult invite() {
        return CommonResult.error(CommonError.NOT_SUPPORT);
    }

    @RequireLogin
    @PostMapping("token/generate")
    public CommonResult generateToken() {
        return null;
    }

    @RequireLogin
    @PostMapping("token/expire")
    public CommonResult expireToken() {
        return null;
    }

    private String getUserIdFromRequest(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        if (Objects.isNull(userId)) {
            throw new BusinessException(CommonError.NOT_AUTHORIZED, "no userId in session");
        }
        return (String) userId;
    }

}
