package com.github.axiangcoding.axbot.server.controller.v1;

import com.github.axiangcoding.axbot.server.controller.entity.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("v1/system")
public class SystemController {

    @GetMapping("version")
    public CommonResult getVersion() {
        return CommonResult.success("version", System.getenv("APP_VERSION"));
    }
}
