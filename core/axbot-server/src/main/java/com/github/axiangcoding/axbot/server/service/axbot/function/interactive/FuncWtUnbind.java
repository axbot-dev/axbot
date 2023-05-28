package com.github.axiangcoding.axbot.server.service.axbot.function.interactive;

import com.github.axiangcoding.axbot.engine.v1.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.QUserSettingService;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class FuncWtUnbind extends AbstractInteractiveFunction {
    @Resource
    KookUserSettingService kookUserSettingService;

    @Resource
    QUserSettingService qUserSettingService;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        kookUserSettingService.unbindWtNickname(input.getUserId());
        return input.response(kookUnbindSuccess());
    }


    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        qUserSettingService.unbindWtNickname(input.getUserId());
        return input.response(cqhttpUnbindSuccess());
    }

    private String kookUnbindSuccess() {
        KookQuickCard card = new KookQuickCard("快捷查询解绑成功", "success");
        return card.displayWithFooter();
    }

    private String cqhttpUnbindSuccess() {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("快捷查询解绑成功");
        return msg.displayWithFooter();
    }
}
