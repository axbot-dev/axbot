package com.github.axiangcoding.axbot.app.bot.function.passive.inject;

import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.enums.FunctionType;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.KOOKMDMessage;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import com.github.axiangcoding.axbot.app.server.data.entity.EndUser;
import com.github.axiangcoding.axbot.app.server.service.EndUserService;
import jakarta.annotation.Resource;
import love.forte.simbot.event.ChannelMessageEvent;

@AxPassiveFunc(command = FunctionType.USER_BANNED)
public class FuncUserBanned extends AbstractPassiveFunction {
    @Resource
    EndUserService endUserService;


    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        String userId = event.getAuthor().getId().toString();

        EndUser endUser = endUserService.getOrCreate(userId, BotPlatform.KOOK);


        KOOKCardTemplate mt = new KOOKCardTemplate("本账号已被拉黑！", "danger");
        mt.addModuleMdSection("本账号因为滥用或者违反规则，已被AXBot拉黑");
        mt.addModuleMdSection("拉黑原因：%s".formatted(
                KOOKMDMessage.code(endUser.getStatus().getBannedReason())
        ));
        mt.addModuleMdSection("拉黑状态将在 %s 后解除".formatted(
                KOOKMDMessage.code(
                        endUser.getStatus().getBannedUntil().toString()
                )
        ));
        mt.addModuleMdSection("如果你对拉黑有任何异议，请申诉");
        mt.addGetHelp("到“#拉黑申述”频道进行申诉");
        event.replyBlocking(toCardMessage(mt.displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        String userId = event.getAuthor().getId().toString();

        EndUser endUser = endUserService.getOrCreate(userId, BotPlatform.QQ_GUILD);

        QGContentTemplate ct = new QGContentTemplate("本账号已被拉黑！");
        ct.addLine("本账号因为滥用或者违反规则，已被AXBot拉黑");
        ct.addLine("拉黑原因：%s".formatted(
                endUser.getStatus().getBannedReason()
        ));
        ct.addLine("拉黑状态将在 %s 后解除".formatted(
                endUser.getStatus().getBannedUntil().toString()
        ));
        ct.addLine("如果你对拉黑有任何异议，请申诉");
        event.replyBlocking(toTextMessage(ct.displayWithFooter()));
    }
}
