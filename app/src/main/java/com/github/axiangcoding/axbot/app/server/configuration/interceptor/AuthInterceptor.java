package com.github.axiangcoding.axbot.app.server.configuration.interceptor;

import com.github.axiangcoding.axbot.app.server.configuration.annotation.RequireApiKey;
import com.github.axiangcoding.axbot.app.server.configuration.annotation.RequireLogin;
import com.github.axiangcoding.axbot.app.server.configuration.exception.BusinessException;
import com.github.axiangcoding.axbot.app.server.controller.entity.CommonError;
import com.github.axiangcoding.axbot.app.server.data.entity.ApiKey;
import com.github.axiangcoding.axbot.app.server.data.entity.AppUser;
import com.github.axiangcoding.axbot.app.server.service.ApiKeyService;
import com.github.axiangcoding.axbot.app.server.service.AppUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;


@Slf4j
@Configuration
public class AuthInterceptor implements HandlerInterceptor {
    @Resource
    ApiKeyService apiKeyService;

    @Resource
    AppUserService appUserService;


    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        checkRequireLogin(request, (HandlerMethod) handler);
        checkApiKey(request, (HandlerMethod) handler);
        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request,
                           @NotNull HttpServletResponse response,
                           @NotNull Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    private void checkRequireLogin(HttpServletRequest request, HandlerMethod handler) {
        RequireLogin annotation = handler.getMethodAnnotation(RequireLogin.class);
        if (annotation == null) {
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new BusinessException(CommonError.NOT_AUTHORIZED);
        }
    }

    private void checkApiKey(HttpServletRequest request, HandlerMethod handler) {
        RequireApiKey annotation = handler.getMethodAnnotation(RequireApiKey.class);
        if (annotation == null) {
            return;
        }
        String key = request.getHeader("api-key");
        Optional<ApiKey> opt = apiKeyService.findByVal(key);

        if (opt.isEmpty()) {
            throw new BusinessException(CommonError.NOT_PERMIT, "api-key not exist");
        }
        if (StringUtils.isBlank(key) || !apiKeyService.keyValid(key)) {
            throw new BusinessException(CommonError.API_KEY_INVALID, "api-key expired");
        }
        if (annotation.superAdmin()) {
            ApiKey apiKey = opt.get();
            Optional<AppUser> optU = appUserService.findByUserId(apiKey.getUserId());
            if (optU.isEmpty()) {
                throw new BusinessException(CommonError.NOT_PERMIT, "user not exist");
            }
            AppUser user = optU.get();
            if (!Boolean.TRUE.equals(user.getIsSuperAdmin())) {
                throw new BusinessException(CommonError.NOT_PERMIT, "api-key owner is not super admin");
            }
        }
    }

}
