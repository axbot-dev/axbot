package com.github.axiangcoding.axbot.app.bot.function.passive;

import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.UserCmd;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import love.forte.simbot.event.ChannelMessageEvent;

@AxPassiveFunc(command = UserCmd.GUILD_BANNED)
public class FuncGuildBanned extends AbstractPassiveFunction {

    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        KOOKCardTemplate mt = new KOOKCardTemplate("本服务器已被拉黑！", "danger");
        mt.addModuleMdSection("本服务器因为滥用或者违反规则，已被AXBot拉黑");
        mt.addModuleMdSection("如果你对本封禁有任何异议，请申诉");
        mt.addGetHelp("到“#拉黑申述”频道进行申诉");
        event.replyBlocking(toCardMessage(mt.displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        QGContentTemplate ct = new QGContentTemplate("本服务器已被拉黑！");
        ct.addLine("本服务器因为滥用或者违反规则，已被AXBot拉黑");
        ct.addLine("如果你对本封禁有任何异议，请申诉");
        event.replyBlocking(toTextMessage(ct.displayWithFooter()));
    }
}
