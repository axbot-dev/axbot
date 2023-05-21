package com.github.axiangcoding.axbot.server.service.axbot.function.interactive;

import com.github.axiangcoding.axbot.engine.v1.SupportPlatform;
import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.data.entity.SponsorOrder;
import com.github.axiangcoding.axbot.server.service.RemoteClientService;
import com.github.axiangcoding.axbot.server.service.SponsorOrderService;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class FuncSponsor extends InteractiveFunction {
    @Resource
    SponsorOrderService sponsorOrderService;

    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    RemoteClientService remoteClientService;

    @Resource
    BotConfProps botConfProps;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        String guildId = input.getGuildId();
        String channelId = input.getChannelId();
        String userId = input.getUserId();
        String orderId = sponsorOrderService.generatePersonalOrder(SupportPlatform.KOOK, guildId, channelId, userId);
        threadPoolTaskExecutor.execute(() -> {
            try {
                int i = 0;
                int maxTimes = 120;
                while (i < maxTimes) {
                    Optional<SponsorOrder> opt = sponsorOrderService.findByOrderId(orderId);
                    if (opt.isPresent()) {
                        SponsorOrder entity = opt.get();
                        if (SponsorOrder.STATUS.SUCCESS.getName().equals(entity.getStatus())) {
                            Integer month = entity.getMonth();
                            String planName = entity.getPlanTitle();
                            remoteClientService.sendKookCardMsg(input.response(sponsorSuccess(month, planName)));
                            break;
                        }
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    i++;
                }
                if (i == maxTimes) {
                    sponsorOrderService.closeOrder(orderId);
                }
            } catch (Exception e) {
                log.error("get sponsor result error", e);
            }
        });
        return input.response(getPanel(orderId));
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        CqhttpQuickMsg msg = new CqhttpQuickMsg("赞助AXBot");
        msg.addLine("如果你想了解赞助方案，请查看文档");
        msg.addLine("如果您选择赞助AxBot，我们感激不尽。");
        return input.response(msg.displayWithFooter());
    }

    public String getPanel(String orderId) {
        KookQuickCard card = new KookQuickCard("赞助AXBot", "success");
        card.addModule(KookCardMessage.quickMdSection("如果您选择赞助AxBot，我们感激不尽。"));
        card.addModule(KookCardMessage.quickMdSection("点击以下链接将跳转到爱发电平台发起赞助，如果选择了带订阅的方案，会自动激活到当前账号或者当前社群中"));
        card.addModule(KookCardMessage.newDivider());

        String url = "https://afdian.net/order/create?plan_id=bf8dd888eed711eda90b52540025c377&custom_order_id=%s";
        card.addModule(KookCardMessage.quickTextLinkSection("赞助个人普通订阅", "个人普通订阅", "primary", url.formatted(orderId)));
        card.addModule(KookCardMessage.quickTextLinkSection("仅赞助（会展示在赞助名单中，无特权）", "赞助", "primary",
                "https://afdian.net/order/create?user_id=966767508b5811eca47c52540025c377"));


        card.addModule(KookCardMessage.newDivider());
        card.addModule(KookCardMessage.quickMdSection("如果赞助过程中发生了任何问题，请联系开发者处理"));
        card.addModule(KookCardMessage.quickTextLinkSection("进入Kook服务器，到#赞助相关频道进行反馈", "进入KOOK服务器", "primary", "https://kook.top/eUTZK7"));
        card.addModule(KookCardMessage.quickTextLinkSection("私信开发者", "点击跳转", "info", "https://www.kookapp.cn/app/home/privatemessage/2936837460"));
        return card.displayWithFooter();
    }

    public String sponsorSuccess(int month, String planName) {
        KookQuickCard card = new KookQuickCard("赞助成功", "success");
        card.addModule(KookCardMessage.quickMdSection("感谢您赞助了 %s 个月的 %s".formatted(
                KookMDMessage.code(String.valueOf(month)), KookMDMessage.code(planName))));
        String command = botConfProps.getTriggerMessagePrefix().get(0) + " 状态";
        card.addModule(KookCardMessage.quickMdSection("如果你想了解自己的订阅状态，请使用命令 %s 查看".formatted(KookMDMessage.code(command))));
        return card.displayWithFooter();
    }
}
