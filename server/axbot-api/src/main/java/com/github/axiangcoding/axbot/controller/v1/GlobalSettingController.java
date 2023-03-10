package com.github.axiangcoding.axbot.controller.v1;

import com.github.axiangcoding.axbot.service.GlobalSettingService;
import com.github.axiangcoding.axbot.entity.CommonResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("v1/settings")
public class GlobalSettingController {
    @Resource
    GlobalSettingService globalSettingService;


    @GetMapping("")
    public CommonResult listAll() {
        Map<String, String> settings = globalSettingService.findAllAsMap();
        return CommonResult.success("settings", settings);
    }

}
