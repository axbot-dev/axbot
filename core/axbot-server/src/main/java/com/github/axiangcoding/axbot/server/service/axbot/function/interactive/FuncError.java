package com.github.axiangcoding.axbot.server.service.axbot.function.interactive;

import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import org.springframework.stereotype.Component;

@Component
public class FuncError extends InteractiveFunction {
    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        KookQuickCard card = new KookQuickCard("服务器内部错误！", "danger");
        card.addModule(KookCardMessage.quickMdSection("服务器内部错误，请稍后重试"));
        card.addModule(KookCardMessage.quickMdSection("如果错误一直存在，请联系开发者"));
        return input.response(card.displayWithFooter());
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("服务器内部错误！");
        msg.addLine("服务器内部错误，请稍后重试");
        msg.addLine("如果错误一直存在，请联系开发者");
        return input.response(msg.displayWithFooter());
    }
}
