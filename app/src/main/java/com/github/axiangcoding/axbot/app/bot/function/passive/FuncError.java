package com.github.axiangcoding.axbot.app.bot.function.passive;

import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.ClickBtnEvent;
import com.github.axiangcoding.axbot.app.bot.enums.UserCmd;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.KOOKMDMessage;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import io.opentelemetry.api.trace.Span;
import love.forte.simbot.event.ChannelMessageEvent;

import java.util.HashMap;
import java.util.Map;

@AxPassiveFunc(command = UserCmd.ERROR)
public class FuncError extends AbstractPassiveFunction {
    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        KOOKCardTemplate mt = new KOOKCardTemplate("服务器内部错误！", "danger");
        mt.addModuleMdSection("服务器发生内部错误，无法处理这条命令");
        String traceId = Span.current().getSpanContext().getTraceId();
        Map<String, String> map = new HashMap<>();
        map.put("type", ClickBtnEvent.BUG_REPORT.name());
        map.put("trace_id", traceId);
        mt.addModuleBtnEvent("快速反馈给开发者", "一键反馈", "info", JSONObject.toJSONString(map));
        mt.addModuleMdSection("或者，你可以输入 %s 来更加详细地反馈错误".formatted(
                KOOKMDMessage.code("反馈 <详细内容>")
        ));
        mt.addModuleMdSection("请务必在 <详细内容> 中带上错误追踪ID：%s".formatted(
                KOOKMDMessage.code(traceId)
        ));
        event.replyBlocking(toCardMessage(mt.displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        QGContentTemplate msg = new QGContentTemplate("服务器内部错误！");
        msg.addLine("服务器内部错误，请稍后重试");
        msg.addLine("如果错误一直存在，请到KOOK频道反馈BUG");
        event.replyBlocking(toTextMessage(msg.displayWithFooter()));
    }
}
