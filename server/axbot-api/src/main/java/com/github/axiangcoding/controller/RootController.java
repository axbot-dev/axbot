package com.github.axiangcoding.controller;

import com.github.axiangcoding.service.GlobalConfigService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("")
public class RootController {

    @Resource
    GlobalConfigService globalConfigService;


}
