package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.remote.qiniu.QiniuClient;
import com.github.axiangcoding.axbot.remote.qiniu.service.entity.QiniuResponse;
import com.github.axiangcoding.axbot.remote.qiniu.service.entity.resp.TextCensorResult;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.data.entity.TextCensor;
import com.github.axiangcoding.axbot.server.data.repository.TextCensorRepository;
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

    public long cleanTextCensorCache() {
        long d1 = textCensorRepository.deleteBySensitive(true);
        long d2 = textCensorRepository.deleteBySensitive(false);
        return d1 + d2;
    }

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
            return !censor.getSensitive();
        }

        boolean passed = checkText(text);
        TextCensor entity = new TextCensor();
        entity.setText(text);
        entity.setSensitive(!passed);
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
