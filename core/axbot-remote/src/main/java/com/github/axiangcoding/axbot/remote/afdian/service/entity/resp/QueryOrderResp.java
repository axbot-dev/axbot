package com.github.axiangcoding.axbot.remote.afdian.service.entity.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class QueryOrderResp {
    List<OrderList> list;

    @Getter
    @Setter
    @ToString
    public static class OrderList {
        String outTradeNo;
        String userId;
        String planId;
        Double month;
        String totalAmount;
        String showAmount;
        Double status;
        String remark;
        String redeemId;
        Double productType;
        String discount;
        // List<?> skuDetail;
        Double createTime;
        String planTitle;
        String userPrivateId;
        String addressPerson;
        String addressPhone;
        String addressAddress;
        String customOrderId;
    }
}
