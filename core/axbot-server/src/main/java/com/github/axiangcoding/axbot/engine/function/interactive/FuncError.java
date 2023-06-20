package com.github.axiangcoding.axbot.engine.function.interactive;

import com.github.axiangcoding.axbot.engine.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.annot.AxbotInteractiveFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.engine.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.engine.template.KookQuickCard;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@AxbotInteractiveFunc(command = InteractiveCommand.ERROR)
@Component
public class FuncError extends AbstractInteractiveFunction {
    @Resource
    BotConfProps botConfProps;


    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        KookQuickCard card = new KookQuickCard("服务器内部错误！", "danger");
        card.addModuleMdSection("AXBot内部错误，这可能是多种原因导致的");
        card.addModuleMdSection("如果错误一直存在，请使用 %s 命令来快捷向开发者反馈问题".formatted(
                KookMDMessage.code(botConfProps.getDefaultTriggerPrefix()+" 反馈 <简单介绍下你的问题>")
        ));
        card.addModuleGetHelp("也可以到“#问题反馈和意见建议”频道直接反馈BUG");
        return input.response(card.displayWithFooter());
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("服务器内部错误！");
        msg.addLine("服务器内部错误，请稍后重试");
        msg.addLine("如果错误一直存在，请到KOOK频道反馈BUG");
        return input.response(msg.display());
    }
}
