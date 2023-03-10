package com.github.axiangcoding.axbot.configuration;

import com.github.axiangcoding.axbot.entity.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public CommonResult exceptionHandler(RuntimeException e) {
        return CommonResult.error(e);
    }

    @ExceptionHandler(value = Exception.class)
    public CommonResult exceptionHandler(Exception e) {
        return CommonResult.error(e);
    }
}
