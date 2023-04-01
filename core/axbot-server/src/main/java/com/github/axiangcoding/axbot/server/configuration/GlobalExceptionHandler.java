package com.github.axiangcoding.axbot.server.configuration;

import com.github.axiangcoding.axbot.server.configuration.exception.BusinessException;
import com.github.axiangcoding.axbot.server.controller.entity.CommonError;
import com.github.axiangcoding.axbot.server.controller.entity.CommonResult;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult exceptionHandler(ConstraintViolationException e) {
        log.warn(e.getMessage(), e);
        Map<String, Object> data = new HashMap<>();
        data.put("errors", e.getConstraintViolations()
                .stream()
                .map(f -> "%s - %s".formatted(f.getPropertyPath(), f.getMessage()))
                .collect(Collectors.toCollection(ArrayList::new)));
        return CommonResult.error(CommonError.INVALID_PARAM, data);
    }

    @ExceptionHandler(BindException.class)
    public CommonResult exceptionHandler(BindException e) {
        log.warn(e.getMessage(), e);
        Map<String, Object> data = new HashMap<>();
        data.put("errors", e.getFieldErrors()
                .stream()
                .map(f -> "%s - %s".formatted(f.getField(), f.getDefaultMessage()))
                .collect(Collectors.toCollection(ArrayList::new)));
        return CommonResult.error(CommonError.INVALID_PARAM, data);
    }

    @ExceptionHandler(BusinessException.class)
    public CommonResult exceptionHandler(BusinessException e) {
        log.warn(e.getMessage(), e);
        return CommonResult.error(e.getCommonError());
    }

    @ExceptionHandler(RuntimeException.class)
    public CommonResult exceptionHandler(RuntimeException e) {
        log.warn(e.getMessage(), e);
        return CommonResult.error(e);
    }

    @ExceptionHandler(Exception.class)
    public CommonResult exceptionHandler(Exception e) {
        log.warn(e.getMessage(), e);
        return CommonResult.error(e);
    }
}
