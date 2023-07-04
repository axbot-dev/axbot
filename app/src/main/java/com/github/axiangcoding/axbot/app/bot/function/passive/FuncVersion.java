package com.github.axiangcoding.axbot.app.bot.function.passive;

import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.UserCmd;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.KOOKMDMessage;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import love.forte.simbot.event.ChannelMessageEvent;

@AxPassiveFunc(command = UserCmd.VERSION)
public class FuncVersion extends AbstractPassiveFunction {
    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        KOOKCardTemplate ct = new KOOKCardTemplate("当前版本", "success");
        ct.addModuleMdSection("当前版本为 " + KOOKMDMessage.code(getVersion()));
        event.replyAsync(toCardMessage(ct.displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        QGContentTemplate ct = new QGContentTemplate("当前版本");
        ct.addLine("当前版本为 " + getVersion());
        event.replyAsync(toTextMessage(ct.displayWithFooter()));
    }

    private static String getVersion() {
        return System.getenv("APP_VERSION");
    }
}
