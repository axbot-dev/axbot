package com.github.axiangcoding.axbot.app.bot.function.passive;

import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.enums.UserCmd;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.KOOKMDMessage;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import com.github.axiangcoding.axbot.app.server.data.entity.EndUser;
import com.github.axiangcoding.axbot.app.server.service.EndUserService;
import jakarta.annotation.Resource;
import love.forte.simbot.event.ChannelMessageEvent;

@AxPassiveFunc(command = UserCmd.USER_USAGE_LIMIT)
public class FuncUserUsageLimit extends AbstractPassiveFunction {

    @Resource
    EndUserService endUserService;

    @Resource
    FuncUserBanned funcUserBanned;

    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        String userId = event.getAuthor().getId().toString();
        EndUser endUser = endUserService.getOrCreate(userId, BotPlatform.KOOK);
        Integer inputToday = endUser.getUsage().getInputToday();
        int inputLimit = endUserService.getInputLimit(userId, BotPlatform.KOOK);
        if (inputToday > inputLimit + 5) {
            endUserService.blockUser(userId, BotPlatform.KOOK, "使用超出限制5次以上");
            funcUserBanned.processForKOOK(event);
            return;
        }
        KOOKCardTemplate ct = new KOOKCardTemplate("使用超限", "warning");
        ct.addModuleMdSection("你在今日使用的次数已经超限，第二天重置");
        ct.addModuleMdSection("当日使用：%s / %s".formatted(
                KOOKMDMessage.code(String.valueOf(inputToday)), KOOKMDMessage.code(String.valueOf(inputLimit))));
        ct.addModuleMdSection("超限5次会被拉黑，请谨慎使用命令");
        event.replyBlocking(toCardMessage(ct.displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        String userId = event.getAuthor().getId().toString();
        EndUser endUser = endUserService.getOrCreate(userId, BotPlatform.QQ_GUILD);
        Integer inputToday = endUser.getUsage().getInputToday();
        int inputLimit = endUserService.getInputLimit(userId, BotPlatform.QQ_GUILD);
        if (inputToday > inputLimit + 5) {
            endUserService.blockUser(userId, BotPlatform.QQ_GUILD, "使用超出限制5次以上");
            funcUserBanned.processForQG(event);
            return;
        }
        QGContentTemplate ct = new QGContentTemplate("使用超限");
        ct.addLine("你在今日使用的次数已经超限，第二天重置");
        ct.addLine("当日使用：%s / %s".formatted(
                String.valueOf(inputToday), String.valueOf(inputLimit)));
        ct.addLine("超限5次会被拉黑，请谨慎使用命令");
        event.replyBlocking(toTextMessage(ct.displayWithFooter()));
    }
}
