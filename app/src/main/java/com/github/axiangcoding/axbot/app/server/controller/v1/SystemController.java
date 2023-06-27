package com.github.axiangcoding.axbot.app.server.controller.v1;


import com.github.axiangcoding.axbot.app.server.controller.entity.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("v1/system")
public class SystemController {

    @Operation(summary = "获取当前版本")
    @GetMapping("version")
    public CommonResult getVersion() {
        return CommonResult.success("version", System.getenv("APP_VERSION"));
    }

    @Operation(summary = "服务状态监测")
    @GetMapping("health")
    public CommonResult health() {
        return CommonResult.success();
    }
}
