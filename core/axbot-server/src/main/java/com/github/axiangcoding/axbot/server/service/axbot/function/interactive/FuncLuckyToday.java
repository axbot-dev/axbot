package com.github.axiangcoding.axbot.server.service.axbot.function.interactive;

import com.github.axiangcoding.axbot.engine.v1.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.v1.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.server.service.axbot.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.server.service.axbot.template.KookQuickCard;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class FuncLuckyToday extends AbstractInteractiveFunction {

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        KookQuickCard quickCard = new KookQuickCard("AXBot 气运", "success");
        int lucky = todayLucky(input.getUserId());

        String text = generateText(lucky);
        quickCard.addModule(KookCardMessage.quickMdSection("你今天的气运值是  %s , %s".formatted(
                KookMDMessage.code(String.valueOf(lucky)), text)));

        return input.response(quickCard.displayWithFooter());
    }


    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        CqhttpQuickMsg quickMsg = new CqhttpQuickMsg("AXBot 气运");
        int lucky = todayLucky(input.getUserId());
        String text = generateText(lucky);

        quickMsg.addLine("你今天的气运值是 %d, %s".formatted(lucky, text));
        return input.response(quickMsg.displayWithFooter());
    }

    public int todayLucky(String userId) {
        String s = userId + LocalDate.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_DATE);
        return new Random(s.hashCode()).nextInt(100);
    }

    private String generateText(int luck) {
        if (luck == 0) {
            return "我测，好一个0！";
        } else if (luck < 30) {
            return "玩战雷玩的";
        } else if (luck < 60) {
            return "哦我的上帝，战雷怎么你了";
        } else if (luck < 80) {
            return "老白金人了，那叫一个地~道";
        } else if (luck < 100) {
            return "狠狠奖励奖励奖励奖励奖励奖励自己！";
        } else if (luck == 100) {
            return "到达最高分，理塘！";
        } else {
            return "";
        }
    }
}
