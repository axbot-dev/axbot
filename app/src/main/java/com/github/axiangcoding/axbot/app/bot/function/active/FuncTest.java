package com.github.axiangcoding.axbot.app.bot.function.active;

import com.github.axiangcoding.axbot.app.bot.enums.ActiveEvent;
import com.github.axiangcoding.axbot.app.bot.annotation.AxActiveFunc;
import com.github.axiangcoding.axbot.app.bot.function.AbstractActiveFunction;
import com.github.axiangcoding.axbot.app.server.configuration.exception.BusinessException;
import com.github.axiangcoding.axbot.app.server.controller.entity.CommonError;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.Identifies;
import love.forte.simbot.bot.Bot;

import java.util.Map;

@AxActiveFunc(event = ActiveEvent.TEST)
@Slf4j
public class FuncTest extends AbstractActiveFunction {
    @Override
    public void processByKOOK(Bot bot, Map<String, Object> params) {
        bot.getGuild(Identifies.ID("5345347076370957")).getChannel(Identifies.ID("7497761681809609")).sendAsync("测试主动消息");
    }

    @Override
    public void processByQG(Bot bot, Map<String, Object> params) {
        throw new BusinessException(CommonError.NOT_SUPPORT);
    }
}
