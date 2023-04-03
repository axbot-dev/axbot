package com.github.axiangcoding.axbot.server.configuration.interceptor;

import com.github.axiangcoding.axbot.server.configuration.annot.RequireApiKey;
import com.github.axiangcoding.axbot.server.configuration.annot.RequireLogin;
import com.github.axiangcoding.axbot.server.configuration.exception.BusinessException;
import com.github.axiangcoding.axbot.server.controller.entity.CommonError;
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
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        boolean check1 = checkRequireLogin(request, (HandlerMethod) handler);
        boolean check2 = checkApiKey(request, (HandlerMethod) handler);

        return check1 && check2;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request,
                           @NotNull HttpServletResponse response,
                           @NotNull Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    private boolean checkRequireLogin(HttpServletRequest request, HandlerMethod handler) {
        RequireLogin annotation = handler.getMethodAnnotation(RequireLogin.class);
        if (annotation == null) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new BusinessException(CommonError.NOT_AUTHORIZED);
        }
        return true;
    }

    private boolean checkApiKey(HttpServletRequest request, HandlerMethod handler) {
        RequireApiKey annotation = handler.getMethodAnnotation(RequireApiKey.class);
        if (annotation == null) {
            return true;
        }
        // TODO
        return true;
    }

}
