package com.github.axiangcoding.axbot.server.service.axbot;

import com.github.axiangcoding.axbot.engine.IAxBotHandlerForCqhttp;
import com.github.axiangcoding.axbot.engine.entity.AxBotUserOutput;
import com.github.axiangcoding.axbot.server.service.axbot.function.LuckyFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AxBotHandlerForCqhttp implements IAxBotHandlerForCqhttp {
    @Override
    public String getDefault() {
        return "你好，我是AXBot";
    }

    @Override
    public String sensitiveInput(long left) {
        return "我不会理会这个命令。这是一次警告。距离拉黑还差 %d 次".formatted(left);
    }

    @Override
    public String reachedGuildLimit(int usage, int limit) {
        return "本群已经达到了使用限制（%d/%d），第二天会重置".formatted(usage, limit);
    }

    @Override
    public String reachedUserLimit(int usage, int limit) {
        return "你已经达到了使用限制（%d/%d），第二天会重置".formatted(usage, limit);
    }

    @Override
    public String userBanned(String reason) {
        return "你已被拉黑，原因：%s".formatted(reason);
    }

    @Override
    public String guildBanned(String reason) {
        return "这个群已被拉黑，因为：%s".formatted(reason);
    }

    @Override
    public String getHelp() {
        return "很抱歉，我在Q上的支持有限，暂未实现";
    }

    @Override
    public String getVersion() {
        return "当前版本为：%s".formatted(System.getenv("APP_VERSION"));
    }

    @Override
    public String getTodayLucky(long seed) {
        return LuckyFunction.todayLuckForCqhttp(seed);
    }

    @Override
    public String commandNotFound(String unknownCmd) {
        return "很抱歉，我在Q上的支持有限，暂未实现该功能";
    }

    @Override
    public String queryWTProfile(String nickname, AxBotUserOutput out) {
        return "很抱歉，我在Q上的支持有限，暂未实现";
    }

    @Override
    public String updateWTProfile(String nickname, AxBotUserOutput out) {
        return "很抱歉，我在Q上的支持有限，暂未实现";
    }

    @Override
    public String getGuildStatus(String guildId) {
        return "很抱歉，我在Q上的支持有限，暂未实现";
    }

    @Override
    public String getUserStatus(String userId) {
        return null;
    }

    @Override
    public String chatWithAI(String userId, String ask) {
        return null;
    }

    @Override
    public String getSponsor(String guildId, String channelId, String userId, AxBotUserOutput output) {
        return null;
    }

    @Override
    public String joinGuild(String guildId) {
        return null;
    }

    @Override
    public void exitGuild(String guildId) {

    }

    @Override
    public String biliLiveRemind(Long roomId, String title, String areaName, String description) {
        return null;
    }

    @Override
    public String sendWtNew(String url, String title, String comment, String posterUrl, String dateStr) {
        return null;
    }
}
