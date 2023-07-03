package com.github.axiangcoding.axbot.app.server.service;

import com.github.axiangcoding.axbot.app.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.app.server.data.entity.TextCensor;
import com.github.axiangcoding.axbot.app.server.data.repo.TextCensorRepository;
import com.github.axiangcoding.axbot.app.third.qiniu.QiniuClient;
import com.github.axiangcoding.axbot.app.third.qiniu.service.entity.QiniuResponse;
import com.github.axiangcoding.axbot.app.third.qiniu.service.entity.resp.TextCensorResult;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TextCensorService {
    @Resource
    TextCensorRepository textCensorRepository;

    @Resource
    QiniuClient qiniuClient;

    @Resource
    BotConfProps botConfProps;


    public boolean checkAndCacheText(String text) {
        if (!botConfProps.getCensor().getEnabled()) {
            return true;
        }

        if (StringUtils.isBlank(text)) {
            return true;
        }

        Optional<TextCensor> opt = textCensorRepository.findByText(text);
        if (opt.isPresent()) {
            TextCensor censor = opt.get();
            return !censor.getIsSensitive();
        }

        boolean passed = checkText(text);
        TextCensor entity = new TextCensor();
        entity.setText(text);
        entity.setIsSensitive(!passed);
        textCensorRepository.save(entity);
        return passed;
    }

    public boolean checkText(String text) {
        QiniuResponse<TextCensorResult> response = qiniuClient.textCensor(text);
        String suggestion = response.getResult().getSuggestion();
        boolean pass = true;
        if ("pass".equals(suggestion)) {
            pass = true;
        } else if ("review".equals(suggestion)) {
            List<TextCensorResult.Scense.Antispam.Detail> details = response.getResult().getScenes().getAntispam().getDetails();
            for (TextCensorResult.Scense.Antispam.Detail detail : details) {
                if ("politics".equals(detail.getLabel())) {
                    pass = false;
                    break;
                }
            }
        } else {
            return false;
        }
        return pass;
    }
}
