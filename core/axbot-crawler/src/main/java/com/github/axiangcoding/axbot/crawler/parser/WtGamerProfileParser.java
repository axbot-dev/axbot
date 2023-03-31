package com.github.axiangcoding.axbot.crawler.parser;

import com.github.axiangcoding.axbot.crawler.entity.ParserResult;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WtGamerProfileParser {
    public static ParserResult parseHtml(String html) {

        Document doc = Jsoup.parse(html);
        return parse(doc);
    }

    public static ParserResult parseUrl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return parse(doc);
    }

    private static ParserResult parse(Document document) {
        ParserResult pr = new ParserResult();
        // 未找到该用户
        Elements e1 = document.select("div[class=user__unavailable]");
        if (e1.size() != 0) {
            pr.setFound(false);
            return pr;
        }
        pr.setFound(true);

        ParserResult.GamerProfile gp = new ParserResult.GamerProfile();
        gp.setNickname(document.select("li[class=user-profile__data-nick]").text());
        gp.setClan(document.select("a[class=user-profile__data-link]").text());

        String clanSuffix = document.select("a[class=user-profile__data-link]").attr("href");
        if (StringUtils.isNotBlank(clanSuffix)) {
            gp.setClanUrl("https://warthunder.com" + clanSuffix);
        }
        gp.setBanned(document.select("div[class=user-profile__data-nick--banned]").size() != 0);
        gp.setRegisterDate(parseRegisterDate(document.select("li[class=user-profile__data-regdate]").text()));
        gp.setTitle(parseTitle(document.select("li[class=user-profile__data-item]").get(0).text()));
        gp.setLevel(parseLevel(document.select("li[class=user-profile__data-item]").get(1).text()));

        List<String> userStatKeys = document
                .select("div[class='user-stat__list-row user-stat__list-row--with-head']>ul[class='user-stat__list user-stat__list--titles']>li")
                .eachText();
        gp.setStatAb(extractUserStat(userStatKeys, document
                .select("div[class='user-stat__list-row user-stat__list-row--with-head']>ul[class~=arcadeFightTab]>li")
                .eachText()));
        gp.setStatRb(extractUserStat(userStatKeys, document
                .select("div[class='user-stat__list-row user-stat__list-row--with-head']>ul[class~=historyFightTab]>li")
                .eachText()));
        gp.setStatSb(extractUserStat(userStatKeys, document
                .select("div[class='user-stat__list-row user-stat__list-row--with-head']>ul[class~=simulationFightTab]>li")
                .eachText()));

        Element aviationElement = document.select("div[class='user-profile__stat user-stat user-stat--tabs']>div[class~=user-stat__list-row]").get(0);
        List<String> userRateAviationKeys = aviationElement.select("ul[class='user-stat__list user-stat__list--titles']>li").eachText();
        gp.setAviationRateAb(extractAviationUserRate(userRateAviationKeys, aviationElement.select("ul[class~=arcadeFightTab]>li")));
        gp.setAviationRateRb(extractAviationUserRate(userRateAviationKeys, aviationElement.select("ul[class~=historyFightTab]>li")));
        gp.setAviationRateSb(extractAviationUserRate(userRateAviationKeys, aviationElement.select("ul[class~=simulationFightTab]>li")));

        Element groundElement = document.select("div[class='user-profile__stat user-stat user-stat--tabs']>div[class~=user-stat__list-row]").get(1);
        List<String> userRateGroundKeys = groundElement.select("ul[class='user-stat__list user-stat__list--titles']>li").eachText();
        gp.setGroundRateAb(extractGroundUserRate(userRateGroundKeys, groundElement.select("ul[class~=arcadeFightTab]>li")));
        gp.setGroundRateRb(extractGroundUserRate(userRateGroundKeys, groundElement.select("ul[class~=historyFightTab]>li")));
        gp.setGroundRateSb(extractGroundUserRate(userRateGroundKeys, groundElement.select("ul[class~=simulationFightTab]>li")));

        Element fleetElement = document.select("div[class='user-profile__stat user-stat user-stat--tabs']>div[class~=user-stat__list-row]").get(2);
        List<String> userRateFleetKeys = fleetElement.select("ul[class='user-stat__list user-stat__list--titles']>li").eachText();
        gp.setFleetRateAb(extractFleetUserRate(userRateFleetKeys, fleetElement.select("ul[class~=arcadeFightTab]>li")));
        gp.setFleetRateRb(extractFleetUserRate(userRateFleetKeys, fleetElement.select("ul[class~=historyFightTab]>li")));
        gp.setFleetRateSb(extractFleetUserRate(userRateFleetKeys, fleetElement.select("ul[class~=simulationFightTab]>li")));
        pr.setProfile(gp);
        return pr;
    }

    /**
     * @param str 形如 “注册日期 14.10.2019”
     * @return
     */
    private static LocalDate parseRegisterDate(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String[] split = str.split(" ");
        String dateStr = split[1];
        return LocalDate.parse(dateStr, formatter);
    }

    private static String parseTitle(String str) {
        return str.replaceAll("\\\\t", "").trim();
    }

    private static Integer parseLevel(String str) {
        String[] split = str.split(" ");
        return Integer.parseInt(split[1]);
    }

    private static Integer parseCommonNumber(String str) {
        if ("N/A".equals(str) || StringUtils.isBlank(str)) {
            return 0;
        }
        return Integer.parseInt(str.replaceAll(",", ""));
    }

    private static Long parseLongNumber(String str) {
        if ("N/A".equals(str) || StringUtils.isBlank(str)) {
            return 0L;
        }
        return Long.parseLong(str.replaceAll(",", ""));
    }

    private static Double parseWinRate(String str) {
        int i = Integer.parseInt(str.replaceAll("%", ""));
        return i * 1.0 / 100;
    }

    private static ParserResult.GamerProfile.UserStat extractUserStat(List<String> userStatKeys, List<String> values) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < userStatKeys.size(); i++) {
            map.put(userStatKeys.get(i), values.get(i));
        }
        ParserResult.GamerProfile.UserStat userStat = new ParserResult.GamerProfile.UserStat();
        userStat.setTotalMission(parseCommonNumber(map.get("任务总数")));
        userStat.setWinRate(parseWinRate(map.get("作战胜率")));
        userStat.setGroundDestroyCount(parseCommonNumber(map.get("地面单位摧毁数")));
        userStat.setFleetDestroyCount(parseCommonNumber(map.get("水面单位摧毁数")));
        userStat.setGameTime(map.get("游戏时间"));
        userStat.setAviationDestroyCount(parseCommonNumber(map.get("空中单位摧毁数")));
        userStat.setWinCount(parseCommonNumber(map.get("胜利场次")));
        userStat.setSliverEagleEarned(parseLongNumber(map.get("银狮获得数")));
        userStat.setDeadCount(parseCommonNumber(map.get("阵亡数")));
        return userStat;
    }

    private static ParserResult.GamerProfile.AviationRate extractAviationUserRate(List<String> userRateKeys, Elements elements) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < userRateKeys.size(); i++) {
            map.put(userRateKeys.get(i), elements.get(i).text());
        }
        ParserResult.GamerProfile.AviationRate userRate = new ParserResult.GamerProfile.AviationRate();
        userRate.setGameCount(parseCommonNumber(map.get("参战次数(空战)")));
        userRate.setFighterGameCount(parseCommonNumber(map.get("参战次数(战斗机)")));
        userRate.setBomberGameCount(parseCommonNumber(map.get("参战次数(轰炸机)")));
        userRate.setAttackerGameCount(parseCommonNumber(map.get("参战次数(攻击机)")));
        userRate.setGameTime(map.get("游戏时长(空战)"));
        userRate.setFighterGameTime(map.get("游戏时长(战斗机)"));
        userRate.setBomberGameTime(map.get("游戏时长(轰炸机)"));
        userRate.setAttackerGameTime(map.get("游戏时长(攻击机)"));
        userRate.setTotalDestroyCount(parseCommonNumber(map.get("击毁目标总计")));
        userRate.setAviationDestroyCount(parseCommonNumber(map.get("空中单位摧毁数")));
        userRate.setGroundDestroyCount(parseCommonNumber(map.get("地面单位摧毁数")));
        userRate.setFleetDestroyCount(parseCommonNumber(map.get("水面单位摧毁数")));
        return userRate;
    }

    private static ParserResult.GamerProfile.GroundRate extractGroundUserRate(List<String> userRateKeys, Elements elements) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < userRateKeys.size(); i++) {
            map.put(userRateKeys.get(i), elements.get(i).text());
        }
        ParserResult.GamerProfile.GroundRate userRate = new ParserResult.GamerProfile.GroundRate();
        userRate.setGameCount(parseCommonNumber(map.get("参战次数(陆战)")));
        userRate.setGroundVehicleGameCount(parseCommonNumber(map.get("参战次数(地面载具)")));
        userRate.setTdGameCount(parseCommonNumber(map.get("参战次数(坦克歼击车)")));
        userRate.setHtGameCount(parseCommonNumber(map.get("参战次数(重型坦克)")));
        userRate.setSpaaGameCount(parseCommonNumber(map.get("参战次数(自行防空炮)")));
        userRate.setGameTime(map.get("游戏时长(陆战)"));
        userRate.setGroundVehicleGameTime(map.get("游戏时长(地面单位)"));
        userRate.setTdGameTime(map.get("游戏时长(坦克歼击车)"));
        userRate.setHtGameTime(map.get("游戏时长(重型坦克)"));
        userRate.setSpaaGameTime(map.get("游戏时长(自行防空炮)"));
        userRate.setTotalDestroyCount(parseCommonNumber(map.get("击毁目标总计")));
        userRate.setAviationDestroyCount(parseCommonNumber(map.get("空中单位摧毁数")));
        userRate.setGroundDestroyCount(parseCommonNumber(map.get("地面单位摧毁数")));
        userRate.setFleetDestroyCount(parseCommonNumber(map.get("水面单位摧毁数")));
        return userRate;
    }

    private static ParserResult.GamerProfile.FleetRate extractFleetUserRate(List<String> userRateKeys, Elements elements) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < userRateKeys.size(); i++) {
            map.put(userRateKeys.get(i), elements.get(i).text());
        }
        ParserResult.GamerProfile.FleetRate fleetRate = new ParserResult.GamerProfile.FleetRate();
        fleetRate.setGameCount(parseCommonNumber(map.get("参战次数(海军)")));
        fleetRate.setFleetGameCount(parseCommonNumber(map.get("参战次数(舰船)")));
        fleetRate.setTorpedoBoatGameCount(parseCommonNumber(map.get("参战次数(鱼雷艇)")));
        fleetRate.setGunboatGameCount(parseCommonNumber(map.get("参战次数(炮艇)")));
        fleetRate.setTorpedoGunboatGameCount(parseCommonNumber(map.get("参战次数(鱼雷炮艇)")));
        fleetRate.setSubmarineHuntGameCount(parseCommonNumber(map.get("参战次数(猎潜艇)")));
        fleetRate.setDestroyerGameCount(parseCommonNumber(map.get("参战次数(驱逐舰)")));
        fleetRate.setNavyBargeGameCount(parseCommonNumber(map.get("参战次数(海军驳渡船)")));
        fleetRate.setGameTime(map.get("游戏时长(海战)"));
        fleetRate.setFleetGameTime(map.get("游戏时长(船舰)"));
        fleetRate.setTorpedoBoatGameTime(map.get("游戏时长(鱼雷艇)"));
        fleetRate.setGunboatGameTime(map.get("游戏时长(炮艇)"));
        fleetRate.setTorpedoGunboatGameTime(map.get("游戏时长(鱼雷炮艇)"));
        fleetRate.setSubmarineHuntGameTime(map.get("游戏时长(猎潜艇)"));
        fleetRate.setDestroyerGameTime(map.get("游戏时长(驱逐舰)"));
        fleetRate.setNavyBargeGameTime(map.get("游戏时长(海军驳渡船)"));
        fleetRate.setTotalDestroyCount(parseCommonNumber(map.get("击毁目标总计")));
        fleetRate.setAviationDestroyCount(parseCommonNumber(map.get("空中单位摧毁数")));
        fleetRate.setGroundDestroyCount(parseCommonNumber(map.get("地面单位摧毁数")));
        fleetRate.setFleetDestroyCount(parseCommonNumber(map.get("水面单位摧毁数")));
        return fleetRate;
    }
}
