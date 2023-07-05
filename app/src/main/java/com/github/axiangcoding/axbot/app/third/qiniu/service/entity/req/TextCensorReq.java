package com.github.axiangcoding.axbot.app.third.qiniu.service.entity.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TextCensorReq {
    Data data = new Data();
    Params params = new Params();

    @Getter
    @Setter
    @ToString
    public static class Data {
        String text;
    }

    @Getter
    @Setter
    @ToString
    public static class Params {
        List<String> scenes;
    }
}
