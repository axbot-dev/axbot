package com.github.axiangcoding.axbot.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CommonResult {
    int code;
    String message;
    Map<String, Object> data;

    public CommonResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResult(CommonError commonError) {
        this.code = commonError.getCode();
        this.message = commonError.getMessage();
    }

    public static CommonResult success() {
        return new CommonResult(CommonError.SUCCESS);
    }

    public static CommonResult success(Map<String, Object> data) {
        CommonResult commonResult = new CommonResult(CommonError.SUCCESS);
        commonResult.setData(data);
        return commonResult;
    }

    public static CommonResult success(String key, Object value) {
        CommonResult commonResult = new CommonResult(CommonError.SUCCESS);
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        commonResult.setData(map);
        return commonResult;
    }

    public static CommonResult error() {
        return new CommonResult(CommonError.ERROR);
    }

    public static CommonResult error(Throwable e) {
        CommonResult commonResult = new CommonResult(CommonError.ERROR);
        commonResult.setMessage(e.getMessage());
        return commonResult;
    }
}
