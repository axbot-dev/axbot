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
public class FuncGuildBanned extends InteractiveFunction {
    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        KookQuickCard card = new KookQuickCard("服务器已被拉黑！", "danger");
        card.addModule(KookCardMessage.quickMdSection("本服务器已被拉黑！"));
        card.addModule(KookCardMessage.quickMdSection("如果你对本封禁有任何异议，请申诉"));
        return input.response(card.displayWithFooter());
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("本群已被拉黑！");
        msg.addLine("本群已被拉黑！");
        msg.addLine("如果你对本封禁有任何异议，请申诉");
        return input.response(msg.displayWithFooter());
    }
}
