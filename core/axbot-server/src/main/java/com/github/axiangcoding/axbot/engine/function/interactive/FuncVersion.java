package com.github.axiangcoding.axbot.engine.function.interactive;

import com.github.axiangcoding.axbot.engine.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.annot.AxbotInteractiveFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.engine.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.engine.template.KookQuickCard;
import org.springframework.stereotype.Component;

@AxbotInteractiveFunc(command = InteractiveCommand.VERSION)
@Component
public class FuncVersion extends AbstractInteractiveFunction {

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
