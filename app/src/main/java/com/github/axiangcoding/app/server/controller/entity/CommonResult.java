package com.github.axiangcoding.app.server.controller.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class CommonResult {
    int code;
    String msg;
    Map<String, Object> data;

    public CommonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommonResult(CommonError commonError) {
        this.code = commonError.getCode();
        this.msg = commonError.getMessage();
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

    public static CommonResult error(CommonError ce) {
        return new CommonResult(ce);
    }

    public static CommonResult error(CommonError ce, String msg) {
        return new CommonResult(ce.getCode(), msg);
    }

    public static CommonResult error(CommonError ce, Map<String, Object> data) {
        return new CommonResult(ce.getCode(), ce.getMessage(), data);
    }

    public static CommonResult error(Map<String, Object> data) {
        CommonResult commonResult = new CommonResult(CommonError.ERROR);
        commonResult.setData(data);
        return commonResult;
    }

    public static CommonResult error(Throwable e) {
        CommonResult commonResult = new CommonResult(CommonError.ERROR);
        commonResult.setMsg(e.getMessage());
        return commonResult;
    }

}
