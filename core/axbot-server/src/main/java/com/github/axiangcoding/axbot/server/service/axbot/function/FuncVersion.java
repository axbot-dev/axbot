package com.github.axiangcoding.axbot.server.service.axbot.function;

import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import org.springframework.stereotype.Component;

@Component
public class FuncVersion extends InteractiveFunction {

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        KookQuickCard quickCard = new KookQuickCard("AXBot 版本", "success");
        quickCard.addModule(KookCardMessage.quickMdSection("当前版本为 " + KookMDMessage.code(System.getenv("APP_VERSION"))));

        return input.response(quickCard.displayWithFooter());
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        CqhttpQuickMsg quickMsg = new CqhttpQuickMsg("AXBot 版本");
        quickMsg.addLine("当前版本为 `%s`".formatted(System.getenv("APP_VERSION")));
        return  input.response(quickMsg.displayWithFooter());
    }
}
