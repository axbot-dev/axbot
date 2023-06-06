package com.github.axiangcoding.axbot.engine.function.interactive;

import com.github.axiangcoding.axbot.engine.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.annot.AxbotInteractiveFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.engine.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.engine.template.KookQuickCard;
import org.springframework.stereotype.Component;

@AxbotInteractiveFunc(command = InteractiveCommand.GUILD_BANNED)
@Component
public class FuncGuildBanned extends AbstractInteractiveFunction {
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