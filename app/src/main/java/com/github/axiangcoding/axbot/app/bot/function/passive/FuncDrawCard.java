package com.github.axiangcoding.axbot.app.bot.function.passive;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.app.bot.annotation.AxPassiveFunc;
import com.github.axiangcoding.axbot.app.bot.enums.UserCmd;
import com.github.axiangcoding.axbot.app.bot.function.AbstractPassiveFunction;
import com.github.axiangcoding.axbot.app.bot.message.template.KOOKCardTemplate;
import com.github.axiangcoding.axbot.app.bot.message.template.QGContentTemplate;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.event.ChannelMessageEvent;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@AxPassiveFunc(command = UserCmd.DRAW_CARD)
@Slf4j
public class FuncDrawCard extends AbstractPassiveFunction {
    @Resource
    ResourceLoader resourceLoader;

    @Override
    public void processForKOOK(ChannelMessageEvent event) {
        JSONObject cardMessage = getRandomCardMessage();
        if (cardMessage == null) {
            KOOKCardTemplate ct = new KOOKCardTemplate("抽卡失败", "danger");
            ct.addModuleMdSection("内部错误");
            event.replyBlocking(toCardMessage(ct.displayWithFooter()));
            return;
        }

        KOOKCardTemplate ct = new KOOKCardTemplate(cardMessage.getString("name"), "success");
        String description = cardMessage.getString("description");
        ct.addModuleMdSection(getRandomDescription(description));
        event.replyBlocking(toCardMessage(ct.displayWithFooter()));
    }

    @Override
    public void processForQG(ChannelMessageEvent event) {
        JSONObject cardMessage = getRandomCardMessage();
        if (cardMessage == null) {
            QGContentTemplate ct = new QGContentTemplate("抽卡失败");
            ct.addLine("内部错误");
            event.replyBlocking(toTextMessage(ct.display()));
            return;
        }
        QGContentTemplate ct = new QGContentTemplate(cardMessage.getString("name"));

        String description = cardMessage.getString("description");
        ct.addLine(getRandomDescription(description));
        event.replyBlocking(toTextMessage(ct.displayWithFooter()));
    }

    private JSONObject getRandomCardMessage() {
        try {
            String content = resourceLoader.getResource("classpath:card/tarot.json").getContentAsString(StandardCharsets.UTF_8);
            JSONArray array = JSONArray.parseArray(content);
            int i = new Random().nextInt(array.size());
            return array.getJSONObject(i);
        } catch (IOException e) {
            log.warn("read tarot.json failed", e);
        }
        return null;
    }

    private String getRandomDescription(String description) {
        int i = new Random().nextInt(2);
        return description.split("\n")[i];
    }
}
