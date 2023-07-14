package com.github.axiangcoding.axbot.app.server.schedule;

import com.github.axiangcoding.axbot.app.bot.enums.ActiveEvent;
import com.github.axiangcoding.axbot.app.bot.enums.BotPlatform;
import com.github.axiangcoding.axbot.app.bot.function.FunctionHandler;
import com.github.axiangcoding.axbot.app.crawler.WtCrawlerClient;
import com.github.axiangcoding.axbot.app.crawler.entity.WTNewParseResult;
import com.github.axiangcoding.axbot.app.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.app.server.data.entity.EndGuild;
import com.github.axiangcoding.axbot.app.server.data.entity.WtNews;
import com.github.axiangcoding.axbot.app.server.service.EndGuildService;
import com.github.axiangcoding.axbot.app.server.service.EndUserService;
import com.github.axiangcoding.axbot.app.server.service.WtNewsService;
import com.github.axiangcoding.axbot.app.third.botmarket.BotMarketClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ScheduleTask {

    @Resource
    EndUserService endUserService;

    @Resource
    EndGuildService endGuildService;

    @Resource
    BotConfProps botConfProps;

    @Resource
    BotMarketClient botMarketClient;

    @Resource
    WtCrawlerClient wtCrawlerClient;

    @Resource
    WtNewsService wtNewsService;

    @Resource
    FunctionHandler functionHandler;


    @Scheduled(cron = "@daily")
    @SchedulerLock(name = "cleanUsage")
    public void cleanUsage() {
        log.info("reset usage");
        endGuildService.resetTodayUsage();
        endUserService.resetTodayUsage();
    }

    /**
     * 设置bot market 在线
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    @SchedulerLock(name = "botMarketOnline")
    public void botMarketOnline() {
        String uuid = botConfProps.getBotMarket().getUuid();
        if (StringUtils.isNotBlank(uuid)) {
            botMarketClient.setOnline(uuid);
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    @SchedulerLock(name = "checkWTLatestNews")
    public void checkWTLatestNews() {
        try {
            List<WTNewParseResult> zhNews = wtCrawlerClient.getNewsFromUrl(WtCrawlerClient.REGION.ZH);
            List<WTNewParseResult> enNews = wtCrawlerClient.getNewsFromUrl(WtCrawlerClient.REGION.EN);
            zhNews.addAll(enNews);
            List<WTNewParseResult> notExistNews = zhNews.stream().filter((item) -> {
                String url = item.getUrl();
                boolean exists = wtNewsService.existsByUrl(url);
                if (!exists) {
                    wtNewsService.save(new WtNews()
                            .setUrl(item.getUrl())
                            .setTitle(item.getTitle())
                            .setPosterUrl(item.getPosterUrl())
                            .setComment(item.getComment())
                            .setDateStr(item.getDateStr()));
                }
                return !exists;
            }).toList();

            List<EndGuild> guilds = endGuildService.findByEnabledWtNewsReminder();
            for (EndGuild guild : guilds) {
                String newsChannelId = guild.getSetting().getWtNewsChannelId();
                if (StringUtils.isEmpty(newsChannelId)) {
                    continue;
                }
                notExistNews.forEach(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("item", item);
                    map.put("guildId", guild.getGuildId());
                    map.put("channelId", guild.getSetting().getWtNewsChannelId());
                    map.put("atRoleId", guild.getSetting().getWtNewsAtRoleId());
                    functionHandler.triggerEvent(ActiveEvent.REMIND_WT_NEWS, BotPlatform.valueOf(guild.getPlatform()), map);
                });
            }


        } catch (IOException e) {
            log.warn("get warthunder news failed", e);
        }
    }
}
