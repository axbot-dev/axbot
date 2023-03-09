package com.github.axiangcoding.service;

import com.github.axiangcoding.entity.GlobalConfig;
import com.github.axiangcoding.repository.GlobalConfigRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalConfigService {
    @Resource
    GlobalConfigRepository globalConfigRepository;


    public List<GlobalConfig> findAll(){
        return globalConfigRepository.findAll();
    }
}
