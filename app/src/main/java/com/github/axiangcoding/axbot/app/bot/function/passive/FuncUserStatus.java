package com.github.axiangcoding.axbot.app.bot.function.passive;

import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.enums.UserCmd;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.KOOKMDMessage;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import com.github.axiangcoding.axbot.app.server.data.entity.EndUser;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.EndUserStatus;
import com.github.axiangcoding.axbot.app.server.data.entity.basic.EndUserUsage;
import com.github.axiangcoding.axbot.app.server.service.EndUserService;
import jakarta.annotation.Resource;
import love.forte.simbot.event.ChannelMessageEvent;

import java.util.LinkedHashMap;
import java.util.Map;

@AxPassiveFunc(command = UserCmd.USER_STATUS)
public class FuncUserStatus extends AbstractPassiveFunction {
    @Resource
    private EndUserService endUserService;

    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        String userId = getUserId(event);
        EndUser endUser = endUserService.getOrCreate(userId, BotPlatform.KOOK);
        LinkedHashMap<String, Object> map = getMap(endUser);

        KOOKCardTemplate ct = new KOOKCardTemplate("当前账号状态", "success");

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            ct.addModuleMdSection("%s: %s".formatted(KOOKMDMessage.bold(entry.getKey()),
                    KOOKMDMessage.code(entry.getValue().toString())));
        }
        event.replyBlocking(toCardMessage(ct.displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        String userId = getUserId(event);
        EndUser endUser = endUserService.getOrCreate(userId, BotPlatform.QQ_GUILD);
        LinkedHashMap<String, Object> map = getMap(endUser);

        QGContentTemplate ct = new QGContentTemplate("当前账号状态");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            ct.addLine("%s: %s".formatted(entry.getKey(), entry.getValue()));
        }
        event.replyBlocking(toTextMessage(ct.displayWithFooter()));
    }

    private LinkedHashMap<String, Object> getMap(EndUser endUser) {
        String userId = endUser.getUserId();
        EndUserUsage usage = endUser.getUsage();
        EndUserStatus status = endUser.getStatus();

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("账号ID", userId);
        map.put("当前状态", status.getStatus());
        map.put("今日输入次数", usage.getInputToday());
        map.put("全部输入次数", usage.getInputTotal());
        map.put("今日请求战雷次数", usage.getQueryWtToday());
        map.put("全部请求战雷次数", usage.getQueryWtTotal());
        return map;
    }
}
