package com.github.axiangcoding.axbot.engine.function.interactive;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.axiangcoding.axbot.engine.InteractiveCommand;
import com.github.axiangcoding.axbot.engine.annot.AxbotInteractiveFunc;
import com.github.axiangcoding.axbot.engine.function.AbstractInteractiveFunction;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.cqhttp.CqhttpInteractiveOutput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveInput;
import com.github.axiangcoding.axbot.engine.io.kook.KookInteractiveOutput;
import com.github.axiangcoding.axbot.engine.template.CqhttpQuickMsg;
import com.github.axiangcoding.axbot.engine.template.KookQuickCard;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@AxbotInteractiveFunc(command = InteractiveCommand.DRAW_CARD)
@Component
@Slf4j
public class FuncDrawCard extends AbstractInteractiveFunction {

    @Resource
    ResourceLoader resourceLoader;

    @Override
    public KookInteractiveOutput execute(KookInteractiveInput input) {
        JSONObject cardMessage = getRandomCardMessage();
        if (cardMessage == null) {
            KookQuickCard quickCard = new KookQuickCard("抽卡失败", "danger");
            quickCard.addModuleMdSection("内部错误");
            return input.response(quickCard.displayWithFooter());
        }
        KookQuickCard quickCard = new KookQuickCard(cardMessage.getString("name"), "success");


        quickCard.addModuleMdSection(cardMessage.getString("description"));
        return input.response(quickCard.displayWithFooter());
    }


    @Override
    public CqhttpInteractiveOutput execute(CqhttpInteractiveInput input) {
        JSONObject cardMessage = getRandomCardMessage();
        if (cardMessage == null) {
            CqhttpQuickMsg quickMsg = new CqhttpQuickMsg("抽卡失败");
            quickMsg.addLine("内部错误");
            return input.response(quickMsg.display());
        }
        CqhttpQuickMsg quickMsg = new CqhttpQuickMsg(cardMessage.getString("name"));
        quickMsg.addLine(cardMessage.getString("description"));
        return input.response(quickMsg.display());
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


}
