package com.github.axiangcoding.axbot.app.bot.function.active;

import com.github.axiangcoding.axbot.app.bot.annotation.AxActiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.ActiveEvent;
import com.github.axiangcoding.axbot.app.bot.function.AbstractActiveFunction;
import com.github.axiangcoding.axbot.app.server.configuration.exception.BusinessException;
import com.github.axiangcoding.axbot.app.server.controller.entity.CommonError;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.bot.Bot;

import java.util.Map;

@AxActiveFunc(event = ActiveEvent.GET_ROLE)
@Slf4j
public class FuncGetRole extends AbstractActiveFunction {
    @Override
    public void processByKOOK(Bot bot, Map<String, Object> params) {
        log.info("params {}", params);
        throw new BusinessException(CommonError.NOT_SUPPORT);
    }

    @Override
    public void processByQG(Bot bot, Map<String, Object> params) {
        log.info("params {}", params);
        throw new BusinessException(CommonError.NOT_SUPPORT);
    }
}
