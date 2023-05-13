package com.github.axiangcoding.axbot.server.service;


import com.github.axiangcoding.axbot.crawler.wt.entity.NewParseResult;
import com.github.axiangcoding.axbot.engine.UserInputCallback;
import com.github.axiangcoding.axbot.engine.entity.AxBotSystemEvent;
import com.github.axiangcoding.axbot.engine.entity.AxBotUserOutput;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotSysInputForKook;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotSysOutputForKook;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotUserInputForKook;
import com.github.axiangcoding.axbot.engine.entity.kook.AxBotUserOutputForKook;
import com.github.axiangcoding.axbot.engine.v1.SupportPlatform;
import com.github.axiangcoding.axbot.remote.bilibili.BiliClient;
import com.github.axiangcoding.axbot.remote.kook.KookClient;
import com.github.axiangcoding.axbot.remote.kook.entity.KookEvent;
import com.github.axiangcoding.axbot.remote.kook.service.entity.req.CreateMessageReq;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.controller.entity.vo.req.KookWebhookEvent;
import com.github.axiangcoding.axbot.server.data.entity.KookGuildSetting;
import com.github.axiangcoding.axbot.server.util.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@Deprecated
public class BotKookService {
    @Resource
    KookClient kookClient;

    @Resource
    BotConfProps botConfProps;

    @Resource
    AxBotService axBotService;

    @Resource
    KookGuildSettingService kookGuildSettingService;

    @Resource
    BiliClient biliClient;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    UserInputRecordService userInputRecordService;

    public boolean compareVerifyToken(String retToken) {
        return StringUtils.equals(retToken, botConfProps.getKook().getVerifyToken());
    }

    /**
     * 判断回调消息类型要进入哪个处理流程
     *
     * @param event
     * @return
     */
    public Map<String, Object> DetermineMessageResponse(KookWebhookEvent event) {
        HashMap<String, Object> map = new HashMap<>();
        KookEvent d = event.getD();
        if (Objects.equals(d.getType(), KookEvent.TYPE_TEXT) ||
                Objects.equals(d.getType(), KookEvent.TYPE_KMARKDOWN)) {
            String content = d.getContent();
            String[] contentSplit = StringUtils.split(content);
            String guildId = d.getExtra().getGuildId();
            String authorId = d.getAuthorId();
            String channelId = d.getTargetId();
            String msgId = d.getMsgId();

            if (axBotService.isTriggerMessage(content)) {
                log.info("received trigger message from kook. user: [{}], message content: [{}]",
                        authorId, content);
                long inputId = userInputRecordService.saveRecordFromKook(authorId, content, guildId, channelId);

                String command = StringUtils.join(Arrays.copyOfRange(contentSplit, 1, contentSplit.length), " ");

                Optional<KookGuildSetting> optKgs = kookGuildSettingService.findBytGuildId(guildId);
                if (optKgs.isEmpty()) {
                    kookGuildSettingService.updateWhenJoin(guildId);
                } else {
                    // TODO 增加使用量
                }
                AxBotUserInputForKook input = new AxBotUserInputForKook();
                input.setFromUserId(authorId);
                input.setRequestCommand(command);
                input.setInputId(inputId);

                input.setFromMsgId(msgId);
                input.setFromChannel(channelId);
                input.setFromGuild(guildId);
                axBotService.genResponseForInputAsync(SupportPlatform.PLATFORM_KOOK, input, new UserInputCallback() {
                    @Override
                    public void callback(AxBotUserOutput output) {
                        if (output == null) {
                            return;
                        }
                        AxBotUserOutputForKook out = ((AxBotUserOutputForKook) output);
                        sendCardMessage(out.getToChannel(), out.getReplayToMsg(), out.getContent());
                    }

                    @Override
                    public void catchException(Exception e) {
                        sendTextMessage(input.getFromChannel(), input.getFromMsgId(), "系统内部错误，请报告开发者");
                    }
                });
            }
        } else if (Objects.equals(d.getType(), KookEvent.TYPE_SYSTEM_MESSAGE)) {

            String channelType = d.getChannelType();
            if (Objects.equals(channelType, KookEvent.CHANNEL_TYPE_WEBHOOK_CHALLENGE)) {
                String challenge = d.getChallenge();
                map.put("challenge", challenge);
            } else if (Objects.equals(channelType, KookEvent.CHANNEL_TYPE_PERSON)) {
                AxBotSysInputForKook input = new AxBotSysInputForKook();
                String type = d.getExtra().getType();
                if ("self_joined_guild".equals(type)) {
                    input.setEvent(AxBotSystemEvent.SYSTEM_EVENT_JOIN_GUILD);
                    String guildId = (String) d.getExtra().getBody().get("guild_id");
                    input.setFromGuild(guildId);
                } else if ("self_exited_guild".equals(type)) {
                    input.setEvent(AxBotSystemEvent.SYSTEM_EVENT_EXIT_GUILD);
                    String guildId = (String) d.getExtra().getBody().get("guild_id");
                    input.setFromGuild(guildId);
                } else {
                    return map;
                }

                axBotService.genResponseForSystemAsync(SupportPlatform.PLATFORM_KOOK, input, output -> {
                    if (output == null) {
                        return;
                    }
                    AxBotSysOutputForKook out = ((AxBotSysOutputForKook) output);
                    sendCardMessage(out.getToChannel(), null, out.getContent());

                });
            }
        } else {
            // do nothing yet
        }
        return map;
    }


    public void sendLatestNews(NewParseResult item) {
        String extraJson = JsonUtils.toJson(item);
        List<KookGuildSetting> guilds = kookGuildSettingService.findByEnableNewsReminder();
        guilds.forEach(guild -> {
            String wtNewsChannelId = guild.getFunctionSetting().getWtNewsChannelId();
            if (StringUtils.isEmpty(wtNewsChannelId)) {
                return;
            }
            AxBotSysInputForKook input = new AxBotSysInputForKook();
            input.setEvent(AxBotSystemEvent.SYSTEM_EVENT_WT_NEWS);
            input.setExtraJson(extraJson);
            axBotService.genResponseForSystemAsync(SupportPlatform.PLATFORM_KOOK, input, output -> {
                if (output == null) {
                    return;
                }
                sendCardMessage(wtNewsChannelId, null, output.getContent());
            });
        });
    }

    public void sendCardMessage(String targetId, String quoteId, String content) {
        CreateMessageReq req = new CreateMessageReq();
        req.setType(KookEvent.TYPE_CARD);
        req.setQuote(quoteId);
        req.setTargetId(targetId);
        req.setContent(content);
        kookClient.createMessage(req);
    }


    public void sendTextMessage(String targetId, String quoteId, String content) {
        CreateMessageReq req = new CreateMessageReq();
        req.setType(KookEvent.TYPE_TEXT);
        req.setTargetId(targetId);
        req.setQuote(quoteId);
        req.setContent(content);
        kookClient.createMessage(req);
    }
}
