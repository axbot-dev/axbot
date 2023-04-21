package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.remote.qiniu.QiniuClient;
import com.github.axiangcoding.axbot.remote.qiniu.service.entity.QiniuResponse;
import com.github.axiangcoding.axbot.remote.qiniu.service.entity.resp.TextCensorResult;
import com.github.axiangcoding.axbot.server.configuration.props.BotConfProps;
import com.github.axiangcoding.axbot.server.data.entity.TextCensor;
import com.github.axiangcoding.axbot.server.data.repository.TextCensorRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TextCensorService {
    @Resource
    TextCensorRepository textCensorRepository;

    @Resource
    QiniuClient qiniuClient;

    @Resource
    BotConfProps botConfProps;

    public boolean isTextPassCheck(String text) {
        if (!botConfProps.getCensor().getEnabled()) {
            return true;
        }

        Optional<TextCensor> opt = textCensorRepository.findByText(text);
        if (opt.isPresent()) {
            TextCensor censor = opt.get();
            return !censor.getSensitive();
        }

        QiniuResponse<TextCensorResult> response = qiniuClient.textCensor(text);
        boolean passed = "pass".equals(response.getResult().getSuggestion());
        TextCensor entity = new TextCensor();
        entity.setText(text);
        entity.setSensitive(!passed);
        textCensorRepository.save(entity);
        return passed;
    }
}
