package com.github.axiangcoding.axbot.app.server.configuration.exception;


import com.github.axiangcoding.axbot.app.server.controller.entity.CommonError;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final CommonError commonError;

    public BusinessException(CommonError ce){
        super(ce.getMessage());
        this.commonError = ce;
    }

    public BusinessException(CommonError ce, String message) {
        super(message);
        this.commonError = ce;
    }
}
