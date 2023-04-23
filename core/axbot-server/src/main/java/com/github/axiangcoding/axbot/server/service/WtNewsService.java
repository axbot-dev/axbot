package com.github.axiangcoding.axbot.server.service;

import com.github.axiangcoding.axbot.server.data.repository.WtNewsRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class WtNewsService {
    @Resource
    WtNewsRepository wtNewsRepository;

}
