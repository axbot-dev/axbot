package com.github.axiangcoding.axbot.configuration;

import com.github.axiangcoding.axbot.entity.CommonError;
import com.github.axiangcoding.axbot.entity.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public CommonResult exceptionHandler(BindException e) {
        HashMap<String, Object> data = new HashMap<>();

        data.put("errors", e.getFieldErrors()
                .stream()
                .map(f-> "%s - %s".formatted(f.getField(), f.getDefaultMessage()))
                .collect(Collectors.toCollection(ArrayList::new)));
        return CommonResult.error(CommonError.INVALID_PARAM, data);
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
