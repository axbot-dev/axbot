package com.github.axiangcoding.axbot.app.server.configuration.interceptor;

import com.github.axiangcoding.axbot.app.server.configuration.annotation.RequireLogin;
import com.github.axiangcoding.axbot.app.server.configuration.exception.BusinessException;
import com.github.axiangcoding.axbot.app.server.controller.entity.CommonError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@Configuration
public class AuthInterceptor implements HandlerInterceptor {
    // @Resource
    // ApiKeyService apiKeyService;


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
            return ;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new BusinessException(CommonError.NOT_AUTHORIZED);
        }
    }

    private void checkApiKey(HttpServletRequest request, HandlerMethod handler) {
        // RequireApiKey annotation = handler.getMethodAnnotation(RequireApiKey.class);
        // if (annotation == null) {
        //     return;
        // }
        // String key = request.getHeader("api-key");
        // if (StringUtils.isBlank(key) || !apiKeyService.isApiKeyValid(key)) {
        //     throw new BusinessException(CommonError.API_KEY_INVALID, "api-key not exist");
        // }
        //
        // if (annotation.admin()) {
        //     Optional<GlobalUser> opt = apiKeyService.findUserByKey(key);
        //     if (opt.isEmpty()) {
        //         throw new BusinessException(CommonError.NOT_PERMIT, "api-key owner is not admin");
        //     } else {
        //         GlobalUser user = opt.get();
        //         if (!Boolean.TRUE.equals(user.getIsAdmin())) {
        //             throw new BusinessException(CommonError.NOT_PERMIT, "api-key owner is not admin");
        //         }
        //     }
        // }
    }

}
