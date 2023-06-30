package com.github.axiangcoding.axbot.app.bot.function.active;

import com.github.axiangcoding.axbot.app.bot.annotation.AxActiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.ActiveEvent;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.function.AbstractActiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.KOOKMDMessage;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.server.configuration.exception.BusinessException;
import com.github.axiangcoding.axbot.app.server.controller.entity.CommonError;
import com.github.axiangcoding.axbot.app.server.data.entity.EndUser;
import com.github.axiangcoding.axbot.app.server.service.BugReportService;
import com.github.axiangcoding.axbot.app.server.service.EndUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.Identifies;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.definition.Contact;

import java.util.List;
import java.util.Map;

@AxActiveFunc(event = ActiveEvent.REPORT_TRACE)
@Slf4j
public class FuncReportTrace extends AbstractActiveFunction {
    @Resource
    private EndUserService endUserService;

    @Resource
    private BugReportService bugReportService;

    @Override
    public void processByKOOK(Bot bot, Map<String, Object> params) {
        KOOKCardTemplate ct = new KOOKCardTemplate("您收到了一条带有链路信息的BUG反馈", "info");
        ct.addModuleContentSection("您收到这条信息是因为您被设置为了AXBot在KOOK平台上的超级管理员");
        String traceId = (String) params.get("trace_id");
        String reportUserId = (String) params.get("user_id");

        ct.addModuleMdSection("用户ID：%s".formatted(KOOKMDMessage.code(reportUserId)));
        ct.addModuleMdSection("反馈的链路追踪ID：%s".formatted(KOOKMDMessage.code(traceId)));

        if (!bugReportService.traceBugExist(traceId)) {
            bugReportService.reportTrace(BotPlatform.KOOK, reportUserId, traceId);
            List<EndUser> superAdmins = endUserService.getSuperAdmins(BotPlatform.KOOK);
            for (EndUser superAdmin : superAdmins) {
                String userId = superAdmin.getUserId();
                Contact contact = bot.getContact(Identifies.ID(userId));
                if (contact != null) {
                    contact.sendAsync(toCardMessage(ct.displayWithFooter()));
                }

            }
        }
    }

    @Override
    public void processByQG(Bot bot, Map<String, Object> params) {
        throw new BusinessException(CommonError.NOT_SUPPORT);
    }
}
