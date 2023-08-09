package com.github.axiangcoding.axbot.app.bot.function.passive.sys;

import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.enums.FunctionType;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import com.github.axiangcoding.axbot.app.server.data.entity.EndUser;
import com.github.axiangcoding.axbot.app.server.service.BugReportService;
import com.github.axiangcoding.axbot.app.server.service.EndUserService;
import jakarta.annotation.Resource;
import love.forte.simbot.Identifies;
import love.forte.simbot.definition.Contact;
import love.forte.simbot.event.ChannelMessageEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@AxPassiveFunc(command = FunctionType.BUG_REPORT)
public class FuncReportBug extends AbstractPassiveFunction {
    @Resource
    EndUserService endUserService;

    @Resource
    BugReportService bugReportService;

    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        String detail = getReportMessage(event);
        if (StringUtils.isBlank(detail)) {
            event.replyBlocking(toCardMessage(kookNoContent().displayWithFooter()));
            return;
        }
        String userId = event.getAuthor().getId().toString();
        String guildId = event.getChannel().getGuildId().toString();
        bugReportService.reportBug(BotPlatform.KOOK, userId, detail);
        List<EndUser> admins = endUserService.getSuperAdmins(BotPlatform.KOOK);

        for (EndUser admin : admins) {
            Contact contact = event.getBot().getContact(Identifies.ID(admin.getUserId()));
            if (contact != null) {
                contact.sendBlocking(
                        toCardMessage(kookReportDetail(userId, guildId, detail).displayWithFooter()));
            }
        }
        event.replyBlocking(toCardMessage(kookBugReportSuccess().displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        String detail = getReportMessage(event);
        if (StringUtils.isBlank(detail)) {
            event.replyBlocking(toTextMessage(qgNoContent().displayWithFooter()));
            return;
        }
        List<EndUser> admins = endUserService.getSuperAdmins(BotPlatform.QQ_GUILD);
        String userId = event.getAuthor().getId().toString();
        String guildId = event.getChannel().getGuildId().toString();
        bugReportService.reportBug(BotPlatform.QQ_GUILD, userId, detail);
        for (EndUser admin : admins) {
            // FIXME 发送私信的功能暂无明确
            Contact contact = event.getBot().getContact(Identifies.ID(admin.getUserId()));
            if (contact != null) {
                contact.sendBlocking(
                        toTextMessage(qgReportDetail(userId, guildId, detail).displayWithFooter()));
            }
        }
        event.replyBlocking(toTextMessage(qgBugReportSuccess().displayWithFooter()));
    }

    private String getReportMessage(ChannelMessageEvent event) {
        String input = getInput(event);
        String[] split = StringUtils.split(input, null, 2);
        return split[split.length - 1];
    }

    private KOOKCardTemplate kookNoContent() {
        KOOKCardTemplate ct = new KOOKCardTemplate("BUG报告失败", "warning");
        ct.addModuleMdSection("BUG报告内容不能为空");
        return ct;
    }

    private QGContentTemplate qgNoContent() {
        QGContentTemplate ct = new QGContentTemplate("BUG报告失败");
        ct.addLine("BUG报告内容不能为空");
        return ct;
    }

    private KOOKCardTemplate kookReportDetail(String userId, String guildId, String content) {
        KOOKCardTemplate ct = new KOOKCardTemplate("您收到了一条BUG反馈", "info");
        ct.addModuleContentSection("您收到这条信息是因为您被设置为了AXBot在KOOK平台上的超级管理员");
        ct.addModuleMdSection("用户ID：" + userId);
        ct.addModuleMdSection("服务器ID：" + guildId);
        ct.addModuleMdSection("反馈内容：" + content);
        return ct;
    }

    private QGContentTemplate qgReportDetail(String userId, String guildId, String content) {
        QGContentTemplate ct = new QGContentTemplate("您收到了一条BUG反馈");
        ct.addLine("您收到这条信息是因为您被设置为了AXBot在QQ群平台上的超级管理员");
        ct.addLine("用户ID：" + userId);
        ct.addLine("服务器ID：" + guildId);
        ct.addLine("反馈内容：" + content);
        return ct;
    }

    private KOOKCardTemplate kookBugReportSuccess() {
        KOOKCardTemplate ct = new KOOKCardTemplate("BUG报告成功", "success");
        ct.addModuleMdSection("感谢您的反馈，超管已收到私信，会尽快处理");
        ct.addModuleMdSection("必要时，超管会私信询问您的问题，敬请留意");
        return ct;
    }

    private QGContentTemplate qgBugReportSuccess() {
        QGContentTemplate ct = new QGContentTemplate("BUG报告成功");
        ct.addLine("感谢您的反馈，超管已收到私信，会尽快处理");
        ct.addLine("必要时，超管会私信询问您的问题，敬请留意");
        return ct;
    }
}
