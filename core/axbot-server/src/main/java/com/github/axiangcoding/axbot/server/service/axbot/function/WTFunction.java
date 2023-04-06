package com.github.axiangcoding.axbot.server.service.axbot.function;


import com.github.axiangcoding.axbot.bot.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.bot.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.data.entity.WtGamerProfile;
import com.google.gson.Gson;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WTFunction {
    public static String profileNotFoundMsg(String nickname, String moreMsg) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("战雷玩家 %s 的数据".formatted(nickname));
        messages.get(0).setTheme("warning");
        messages.get(0).getModules().add(KookCardMessage.newSection(KookCardMessage.newKMarkdown(moreMsg)));
        return new Gson().toJson(messages);
    }

    public static String profileNotFoundMsg(String nickname) {
        return profileNotFoundMsg(nickname, "该玩家未找到");
    }

    public static String profileFound(String nickname, WtGamerProfile profile) {
        String title = "战雷玩家 %s 的数据".formatted(nickname);
        String theme = "success";
        List<KookCardMessage> messages = KookCardMessage.defaultMsg(title);
        messages.get(0).setTheme(theme);
        List<KookCardMessage> modules = messages.get(0).getModules();
        modules.add(KookCardMessage.newContext(
                List.of(KookCardMessage.newKMarkdown("一切数据均来自战雷官网，AXBot只做计算不做修改"))));

        modules.add(KookCardMessage.newContext(
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


        modules.add(KookCardMessage.newSection(KookCardMessage.newParagraph(3, f1)));
        modules.add(KookCardMessage.newDivider());
        modules.add(KookCardMessage.newSection(KookCardMessage.newParagraph(3, f2)));
        modules.add(KookCardMessage.newDivider());
        modules.add(KookCardMessage.newContext(List.of(KookCardMessage.newKMarkdown("击杀数/出击数简称为'KA'"))));
        modules.add(KookCardMessage.newSection(KookCardMessage.newParagraph(3, f3)));
        return new Gson().toJson(messages);
    }

    public static String profileInQuery(String nickname) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("战雷玩家 %s 的数据".formatted(nickname));
        messages.get(0).setTheme("success");
        List<KookCardMessage> modules = messages.get(0).getModules();
        modules.add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("正在发起查询...请耐心等待")));
        LocalDateTime now = LocalDateTime.now();
        modules.add(KookCardMessage.newCountDown("second", now, now.plusSeconds(60)));

        return new Gson().toJson(messages);
    }

    public static String profileQueryFailed(String nickname) {
        List<KookCardMessage> messages = KookCardMessage.defaultMsg("战雷玩家 %s 的数据".formatted(nickname));
        messages.get(0).setTheme("danger");
        messages.get(0).getModules().add(KookCardMessage.newSection(KookCardMessage.newKMarkdown("查询失败！请稍后重试")));
        return new Gson().toJson(messages);
    }
}
