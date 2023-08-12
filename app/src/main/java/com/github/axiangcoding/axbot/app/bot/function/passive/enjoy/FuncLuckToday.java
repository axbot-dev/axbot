package com.github.axiangcoding.axbot.app.bot.function.passive.enjoy;

import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.enums.FunctionType;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import com.github.axiangcoding.axbot.app.server.data.cache.CacheKeyGenerator;
import jakarta.annotation.Resource;
import love.forte.simbot.event.ChannelMessageEvent;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

@AxPassiveFunc(command = FunctionType.LUCKY_TODAY)
public class FuncLuckToday extends AbstractPassiveFunction {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        KOOKCardTemplate ct = new KOOKCardTemplate("每日气运", "success");
        int lucky = todayLucky(BotPlatform.KOOK, getUserId(event));
        String text = generateText(lucky);
        ct.addModuleMdSection("你今天的气运值是 %s , %s".formatted(
                lucky, text));
        event.replyBlocking(toCardMessage(ct.displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        QGContentTemplate ct = new QGContentTemplate("每日气运");
        int lucky = todayLucky(BotPlatform.QQ_GUILD, getUserId(event));
        String text = generateText(lucky);
        ct.addLine("你今天的气运值是 %s , %s".formatted(
                lucky, text));
        event.replyBlocking(toTextMessage(ct.displayWithFooter()));
    }

    private int todayLucky(BotPlatform platform, String userId) {
        String key = CacheKeyGenerator.getUserLuckKey(platform, userId);
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            String value = stringRedisTemplate.opsForValue().get(key);
            if (value == null) {
                return 0;
            }
            return Integer.parseInt(value);
        } else {
            int luck;
            if (new Random().nextInt(500) == 0) {
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
