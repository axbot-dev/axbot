package com.github.axiangcoding.axbot.app.server.service;

import com.github.axiangcoding.axbot.app.server.data.entity.WtNews;
import com.github.axiangcoding.axbot.app.server.data.repo.WtNewsRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WtNewsService {
    @Resource
    WtNewsRepository wtNewsRepository;


    public boolean existsByUrl(String url) {
        return wtNewsRepository.existsByUrl(url);
    }

    public WtNews save(WtNews wtNews) {
        return wtNewsRepository.save(wtNews);
    }
}
