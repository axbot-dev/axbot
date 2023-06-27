package com.github.axiangcoding.axbot.app.server.service;

import com.github.axiangcoding.axbot.app.server.data.repo.AppSettingRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AppSettingService {
    @Resource
    AppSettingRepository repository;
}
