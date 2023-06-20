package com.github.axiangcoding.axbot.engine.function.interactive;

import com.github.axiangcoding.axbot.engine.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.annot.AxbotInteractiveFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.server.service.KookUserSettingService;
import com.github.axiangcoding.axbot.server.service.QUserSettingService;
import com.github.axiangcoding.axbot.engine.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.engine.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@AxbotInteractiveFunc(command = InteractiveCommand.WT_UNBIND_PROFILE)
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
        return msg.display();
    }
}
