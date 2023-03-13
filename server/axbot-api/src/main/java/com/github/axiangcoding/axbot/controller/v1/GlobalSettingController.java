package com.github.axiangcoding.axbot.controller.v1;

import com.github.axiangcoding.axbot.entity.vo.GlobalSetting;
import com.github.axiangcoding.axbot.service.GlobalSettingService;
import com.github.axiangcoding.axbot.entity.CommonResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/save")
    public CommonResult saveSetting(@Valid @RequestBody GlobalSetting obj) {
        com.github.axiangcoding.axbot.entity.GlobalSetting gs = new com.github.axiangcoding.axbot.entity.GlobalSetting();
        gs.setKey(obj.getKey());
        gs.setValue(obj.getValue());
        gs.setRemark(obj.getRemark());
        globalSettingService.save(gs);
        return CommonResult.success();
    }

}
