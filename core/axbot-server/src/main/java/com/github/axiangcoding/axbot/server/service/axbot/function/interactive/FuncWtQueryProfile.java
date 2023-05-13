package com.github.axiangcoding.axbot.server.service.axbot.function.interactive;

import com.github.axiangcoding.axbot.engine.v1.function.InteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.InteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.data.entity.WtGamerProfile;
import com.github.axiangcoding.axbot.server.service.WTGameProfileService;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
public class FuncWtQueryProfile extends InteractiveFunction {
    @Resource
    WTGameProfileService wtGameProfileService;


    @Resource
    FuncWtUpdateProfile funcWtUpdateProfile;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        String nickname = getUserNickname(input);
        Optional<WtGamerProfile> optGp = wtGameProfileService.findByNickname(nickname);
        if (optGp.isEmpty()) {
            boolean canBeRefresh = wtGameProfileService.canBeRefresh(nickname);
            if (!canBeRefresh) {
                return input.response(kookProfileNotFound(nickname, "未找到该玩家"));
            }
            return funcWtUpdateProfile.execute(input);
        } else {
            return input.response(kookProfileFound(nickname, optGp.get()));
        }
    }

    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        String nickname = getUserNickname(input);
        Optional<WtGamerProfile> optGp = wtGameProfileService.findByNickname(nickname);
        if (optGp.isEmpty()) {
            boolean canBeRefresh = wtGameProfileService.canBeRefresh(nickname);
            if (!canBeRefresh) {
                return input.response(kookProfileNotFound(nickname, "未找到该玩家"));
            }
            return funcWtUpdateProfile.execute(input);
        } else {
            return input.response(cqhttpProfileFound(nickname, optGp.get()));
        }
    }


    private String getUserNickname(InteractiveInput input) {
        String[] paramList = input.getParamList();
        return StringUtils.join(paramList, " ");
    }

    public static String kookProfileNotFound(String nickname, String moreMsg) {
        KookQuickCard quickCard = new KookQuickCard("战雷玩家 %s 的数据".formatted(nickname), "warning");
        quickCard.addModule(KookCardMessage.quickMdSection(moreMsg));
        return quickCard.displayWithFooter();
    }

    public static String cqhttpProfileNotFound(String nickname, String moreMsg) {
        CqhttpQuickMsg quickMsg = new CqhttpQuickMsg("战雷玩家 %s 的数据".formatted(nickname));
        quickMsg.addLine(moreMsg);
        return quickMsg.displayWithFooter();
    }

    public static String kookProfileFound(String nickname, WtGamerProfile profile) {
        KookQuickCard quickCard = new KookQuickCard("战雷玩家 %s 的数据".formatted(nickname), "success");
        quickCard.addModule(KookCardMessage.newContext(
                List.of(KookCardMessage.newKMarkdown("一切数据均来自战雷官网，AXBot只做计算不做修改"))));
        quickCard.addModule(KookCardMessage.newContext(
                List.of(KookCardMessage.newKMarkdown("数据最后更新于 %s".formatted(
                        new PrettyTime(Locale.CHINA).format(profile.getUpdateTime()))))));

        ArrayList<KookCardMessage> f1 = new ArrayList<>();

        f1.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("联队") + "\n" + profile.getClan()));
        f1.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("昵称") + "\n" + profile.getNickname()));
        f1.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("称号") + "\n" + profile.getTitle()));
        f1.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("等级") + "\n" + profile.getLevel()));
        f1.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("注册时间") + "\n" + profile.getRegisterDate()));
        if (profile.getBanned()) {
            f1.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("当前状态") + "\n" + KookMDMessage.colorful("已封禁", "danger")));
        }


        WtGamerProfile.UserStat ab = profile.getStatAb();
        WtGamerProfile.UserStat sb = profile.getStatSb();
        WtGamerProfile.UserStat rb = profile.getStatRb();

        DecimalFormat df = new DecimalFormat("0.00");
        double abKd = 1.0 * (ab.getGroundDestroyCount() + ab.getFleetDestroyCount() + ab.getAviationDestroyCount()) / ab.getDeadCount();
        double sbKd = 1.0 * (sb.getGroundDestroyCount() + sb.getFleetDestroyCount() + sb.getAviationDestroyCount()) / sb.getDeadCount();
        double rbKd = 1.0 * (rb.getGroundDestroyCount() + rb.getFleetDestroyCount() + rb.getAviationDestroyCount()) / rb.getDeadCount();

        ArrayList<KookCardMessage> f2 = new ArrayList<>();
        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("街机任务数") + "\n" + ab.getTotalMission()));
        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("街机胜率") + "\n" + NumberFormat.getPercentInstance().format(ab.getWinRate())));
        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("街机KD") + "\n" + df.format(abKd)));

        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("史实任务数") + "\n" + rb.getTotalMission()));
        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("史实胜率") + "\n" + NumberFormat.getPercentInstance().format(rb.getWinRate())));
        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("史实KD") + "\n" + df.format(rbKd)));

        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("全真任务数") + "\n" + sb.getTotalMission()));
        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("全真胜率") + "\n" + NumberFormat.getPercentInstance().format(sb.getWinRate())));
        f2.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("全真KD") + "\n" + df.format(sbKd)));

        ArrayList<KookCardMessage> f3 = new ArrayList<>();
        WtGamerProfile.AviationRate aab = profile.getAviationRateAb();
        WtGamerProfile.AviationRate arb = profile.getAviationRateRb();
        WtGamerProfile.AviationRate asb = profile.getAviationRateSb();
        double aabKa = 1.0 * aab.getTotalDestroyCount() / aab.getGameCount();
        double arbKa = 1.0 * arb.getTotalDestroyCount() / arb.getGameCount();
        double asbKa = 1.0 * asb.getTotalDestroyCount() / asb.getGameCount();


        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("空战街机KA") + "\n" + df.format(aabKa)));
        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("空战史实KA") + "\n" + df.format(arbKa)));
        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("空战全真KA") + "\n" + df.format(asbKa)));

        WtGamerProfile.GroundRate gab = profile.getGroundRateAb();
        WtGamerProfile.GroundRate grb = profile.getGroundRateRb();
        WtGamerProfile.GroundRate gsb = profile.getGroundRateSb();
        double gabKa = 1.0 * gab.getTotalDestroyCount() / gab.getGameCount();
        double grbKa = 1.0 * grb.getTotalDestroyCount() / grb.getGameCount();
        double gsbKa = 1.0 * gsb.getTotalDestroyCount() / gsb.getGameCount();

        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("陆战街机KA") + "\n" + df.format(gabKa)));
        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("陆战史实KA") + "\n" + df.format(grbKa)));
        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("陆战全真KA") + "\n" + df.format(gsbKa)));


        WtGamerProfile.FleetRate fab = profile.getFleetRateAb();
        WtGamerProfile.FleetRate frb = profile.getFleetRateRb();
        WtGamerProfile.FleetRate fsb = profile.getFleetRateSb();
        double fabKa = 1.0 * fab.getTotalDestroyCount() / fab.getGameCount();
        double frbKa = 1.0 * frb.getTotalDestroyCount() / frb.getGameCount();
        double fsbKa = 1.0 * fsb.getTotalDestroyCount() / fsb.getGameCount();
        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("海战街机KA") + "\n" + df.format(fabKa)));
        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("海战史实KA") + "\n" + df.format(frbKa)));
        f3.add(KookCardMessage.newKMarkdown(KookMDMessage.bold("海战全真KA") + "\n" + df.format(fsbKa)));


        quickCard.addModule(KookCardMessage.newSection(KookCardMessage.newParagraph(3, f1)));
        quickCard.addModule(KookCardMessage.newDivider());
        quickCard.addModule(KookCardMessage.newSection(KookCardMessage.newParagraph(3, f2)));
        quickCard.addModule(KookCardMessage.newDivider());
        quickCard.addModule(KookCardMessage.newContext(List.of(KookCardMessage.newKMarkdown("击杀数/出击数简称为'KA'"))));
        quickCard.addModule(KookCardMessage.newSection(KookCardMessage.newParagraph(3, f3)));
        return quickCard.displayWithFooter();
    }

    public static String cqhttpProfileFound(String nickname, WtGamerProfile profile) {
        CqhttpQuickMsg quickMsg = new CqhttpQuickMsg("战雷玩家 %s 的数据".formatted(nickname));
        quickMsg.addLine("一切数据均来自战雷官网，AXBot只做计算不做修改");
        quickMsg.addLine("数据最后更新于 %s".formatted(
                new PrettyTime(Locale.CHINA).format(profile.getUpdateTime())));
        quickMsg.addLine("联队: %s".formatted(profile.getClan()));
        quickMsg.addLine("昵称: %s".formatted(profile.getNickname()));
        quickMsg.addLine("等级: %s".formatted(profile.getLevel()));
        quickMsg.addLine("称号: %s".formatted(profile.getTitle()));
        quickMsg.addLine("注册时间: %s".formatted(profile.getRegisterDate()));
        if (profile.getBanned()) {
            quickMsg.addLine("当前状态: %s".formatted("封禁中"));
        }
        quickMsg.addDivider();


        WtGamerProfile.UserStat ab = profile.getStatAb();
        WtGamerProfile.UserStat sb = profile.getStatSb();
        WtGamerProfile.UserStat rb = profile.getStatRb();

        DecimalFormat df = new DecimalFormat("0.00");
        double abKd = 1.0 * (ab.getGroundDestroyCount() + ab.getFleetDestroyCount() + ab.getAviationDestroyCount()) / ab.getDeadCount();
        double sbKd = 1.0 * (sb.getGroundDestroyCount() + sb.getFleetDestroyCount() + sb.getAviationDestroyCount()) / sb.getDeadCount();
        double rbKd = 1.0 * (rb.getGroundDestroyCount() + rb.getFleetDestroyCount() + rb.getAviationDestroyCount()) / rb.getDeadCount();
        quickMsg.addLine("街机任务数: %s".formatted(ab.getTotalMission()));
        quickMsg.addLine("街机胜率: %s".formatted(NumberFormat.getPercentInstance().format(ab.getWinRate())));
        quickMsg.addLine("街机KD: %s".formatted(df.format(abKd)));
        quickMsg.addLine("史实任务数: %s".formatted(rb.getTotalMission()));
        quickMsg.addLine("史实胜率: %s".formatted(NumberFormat.getPercentInstance().format(rb.getWinRate())));
        quickMsg.addLine("史实KD: %s".formatted(df.format(rbKd)));
        quickMsg.addLine("全真任务数: %s".formatted(sb.getTotalMission()));
        quickMsg.addLine("全真胜率: %s".formatted(NumberFormat.getPercentInstance().format(sb.getWinRate())));
        quickMsg.addLine("全真KD: %s".formatted(df.format(sbKd)));
        quickMsg.addDivider();
        ArrayList<KookCardMessage> f3 = new ArrayList<>();
        WtGamerProfile.AviationRate aab = profile.getAviationRateAb();
        WtGamerProfile.AviationRate arb = profile.getAviationRateRb();
        WtGamerProfile.AviationRate asb = profile.getAviationRateSb();
        double aabKa = 1.0 * aab.getTotalDestroyCount() / aab.getGameCount();
        double arbKa = 1.0 * arb.getTotalDestroyCount() / arb.getGameCount();
        double asbKa = 1.0 * asb.getTotalDestroyCount() / asb.getGameCount();

        quickMsg.addLine("空战街机KA: %s".formatted(df.format(aabKa)));
        quickMsg.addLine("空战史实KA: %s".formatted(df.format(arbKa)));
        quickMsg.addLine("空战全真KA: %s".formatted(df.format(asbKa)));
        quickMsg.addDivider();

        WtGamerProfile.GroundRate gab = profile.getGroundRateAb();
        WtGamerProfile.GroundRate grb = profile.getGroundRateRb();
        WtGamerProfile.GroundRate gsb = profile.getGroundRateSb();
        double gabKa = 1.0 * gab.getTotalDestroyCount() / gab.getGameCount();
        double grbKa = 1.0 * grb.getTotalDestroyCount() / grb.getGameCount();
        double gsbKa = 1.0 * gsb.getTotalDestroyCount() / gsb.getGameCount();

        quickMsg.addLine("陆战街机KA: %s".formatted(df.format(gabKa)));
        quickMsg.addLine("陆战史实KA: %s".formatted(df.format(grbKa)));
        quickMsg.addLine("陆战全真KA: %s".formatted(df.format(gsbKa)));
        quickMsg.addDivider();

        WtGamerProfile.FleetRate fab = profile.getFleetRateAb();
        WtGamerProfile.FleetRate frb = profile.getFleetRateRb();
        WtGamerProfile.FleetRate fsb = profile.getFleetRateSb();
        double fabKa = 1.0 * fab.getTotalDestroyCount() / fab.getGameCount();
        double frbKa = 1.0 * frb.getTotalDestroyCount() / frb.getGameCount();
        double fsbKa = 1.0 * fsb.getTotalDestroyCount() / fsb.getGameCount();

        quickMsg.addLine("海战街机KA: %s".formatted(df.format(fabKa)));
        quickMsg.addLine("海战史实KA: %s".formatted(df.format(frbKa)));
        quickMsg.addLine("海战全真KA: %s".formatted(df.format(fsbKa)));
        return quickMsg.displayWithFooter();
    }
}
