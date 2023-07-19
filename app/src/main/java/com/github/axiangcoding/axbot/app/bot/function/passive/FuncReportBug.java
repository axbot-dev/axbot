package com.github.axiangcoding.axbot.app.bot.function.passive;

import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.enums.UserCmd;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.server.data.entity.EndUser;
import com.github.axiangcoding.axbot.app.server.service.EndUserService;
import jakarta.annotation.Resource;
import love.forte.simbot.Identifies;
import love.forte.simbot.definition.Contact;
import love.forte.simbot.event.ChannelMessageEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@AxPassiveFunc(command = UserCmd.BUG_REPORT)
public class FuncReportBug extends AbstractPassiveFunction {
    @Resource
    EndUserService endUserService;

    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        String detail = getReportMessage(event);
        if (StringUtils.isBlank(detail)) {
            event.replyBlocking(toCardMessage(noContent().displayWithFooter()));
            return;
        }
        List<EndUser> admins = endUserService.getSuperAdmins(BotPlatform.KOOK);
        String userId = event.getAuthor().getId().toString();
        String guildId = event.getChannel().getGuildId().toString();
        for (EndUser admin : admins) {
            Contact contact = event.getBot().getContact(Identifies.ID(admin.getUserId()));
            if (contact != null) {
                contact.sendBlocking(
                        toCardMessage(reportDetail(userId, guildId, detail).displayWithFooter()));
            }
        }
        event.replyBlocking(toCardMessage(bugReportSuccess().displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {

    }

    private String getReportMessage(ChannelMessageEvent event) {
        String input = getInput(event);
        String[] split = StringUtils.split(input, null, 2);
        return split[split.length - 1];
    }

    private KOOKCardTemplate noContent() {
        KOOKCardTemplate ct = new KOOKCardTemplate("BUG报告失败", "warning");
        ct.addModuleMdSection("BUG报告内容不能为空");
        return ct;
    }

    private KOOKCardTemplate reportDetail(String userId, String guildId, String content) {
        KOOKCardTemplate ct = new KOOKCardTemplate("您收到了一条BUG反馈", "info");
        ct.addModuleContentSection("您收到这条信息是因为您被设置为了AXBot在KOOK平台上的超级管理员");
        ct.addModuleMdSection("用户ID：" + userId);
        ct.addModuleMdSection("服务器ID：" + guildId);
        ct.addModuleMdSection("反馈内容：" + content);
        return ct;
    }

    private KOOKCardTemplate bugReportSuccess() {
        KOOKCardTemplate ct = new KOOKCardTemplate("BUG报告成功", "success");
        ct.addModuleMdSection("感谢您的反馈，超管已收到私信，会尽快处理");
        ct.addModuleMdSection("必要时，超管会私信询问您的问题，敬请留意");
        return ct;
    }
}
