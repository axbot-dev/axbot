package com.github.axiangcoding.axbot.engine.function.interactive;

import com.github.axiangcoding.axbot.engine.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.SupportPlatform;
import com.github.axiangcoding.axbot.engine.annot.AxbotInteractiveFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.remote.kook.entity.KookCardMessage;
import com.github.axiangcoding.axbot.remote.kook.entity.KookMDMessage;
import com.github.axiangcoding.axbot.engine.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.engine.template.KookQuickCard;
import com.github.axiangcoding.axbot.server.cache.CacheKeyGenerator;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@AxbotInteractiveFunc(command = InteractiveCommand.LUCKY)
@Component
public class FuncLuckyToday extends AbstractInteractiveFunction {
    @Resource
    StringRedisTemplate stringRedisTemplate;


    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        KookQuickCard quickCard = new KookQuickCard("AXBot 气运", "success");
        int lucky = todayLucky(SupportPlatform.KOOK, input.getUserId());
        String text = generateText(lucky);
        quickCard.addModule(KookCardMessage.quickMdSection("你今天的气运值是  %s , %s".formatted(
                KookMDMessage.code(String.valueOf(lucky)), text)));

        return input.response(quickCard.displayWithFooter());
    }


    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        CqhttpQuickMsg quickMsg = new CqhttpQuickMsg("AXBot 气运");
        int lucky = todayLucky(SupportPlatform.CQHTTP, input.getUserId());
        String text = generateText(lucky);

        quickMsg.addLine("你今天的气运值是 %d, %s".formatted(lucky, text));
        return input.response(quickMsg.displayWithFooter());
    }

    @Deprecated
    public int todayLucky(String userId) {
        String s = userId + LocalDate.now(ZoneId.of("UTC+8")).format(DateTimeFormatter.ISO_DATE);
        return new Random(s.hashCode()).nextInt(100);
    }

    public int todayLucky(SupportPlatform platform, String userId) {
        String key = CacheKeyGenerator.getUserLuckKey(platform, userId);
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            String value = stringRedisTemplate.opsForValue().get(key);
            if (value == null) {
                return 0;
            }
            return Integer.parseInt(value);
        } else {
            int luck;
            if (new Random().nextInt(1000) == 0) {
                luck = 114514;
            } else {
                luck = new Random().nextInt(100);
            }
            LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC+8"));
            LocalDateTime tomorrow = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            stringRedisTemplate.opsForValue().set(key, String.valueOf(luck), Duration.between(now, tomorrow));
            return luck;
        }

    }

    private String generateText(int luck) {
        if (luck == 0) {
            return "你是0，谁是1？";
        } else if (luck < 2) {
            return "我是1，谁是0？";
        } else if (luck < 30) {
            return "老实交代，酋长派你来这干嘛";
        } else if (luck < 60) {
            return "哦我的上帝，战雷怎么你了";
        } else if (luck < 80) {
            return "老白金人了，那叫一个地~道";
        } else if (luck < 100) {
            return "狠狠奖励奖励奖励自己";
        } else if (luck == 100) {
            return "到达最高分，理塘";
        } else if (luck == 114514) {
            return "今年24岁，是运气王";
        }
        return "我测，我测不出来你的气运了";
    }
}
