package com.github.axiangcoding.axbot.server.controller.entity.vo.req;

import lombok.Data;

@Data
public class AFDianWebhook {
    Integer ec;
    String em;
    IData data;

    @Data
    public static class IData {
        String type;
        Order order;

        @Data
        public static class Order {
            String outTradeNo;
            String userId;
            String planId;
            Integer month;
            String totalAmount;
            String showAmount;
            Integer status;
            String remark;
            String redeemId;
            Integer productType;
            String discount;
            Long createTime;
            String planTitle;
            String userPrivateId;
            String customOrderId;
        }
    }
}

//{
//   "ec": 200,
//   "em": "ok",
//   "data": {
//     "type": "order",
//     "order": {
//       "out_trade_no": "20230510120419511019756935",
//       "user_id": "966767508b5811eca47c52540025c377",
//       "plan_id": "bf8dd888eed711eda90b52540025c377",
//       "month": 1,
//       "total_amount": "5.00",
//       "show_amount": "5.00",
//       "status": 2,
//       "remark": "",
//       "redeem_id": "",
//       "product_type": 0,
//       "discount": "0.00",
//       "sku_detail": [],
//       "create_time": 1683691459,
//       "plan_title": "\u666e\u901a\u4e2a\u4eba\u8d5e\u52a9",
//       "user_private_id": "006d416a27516dbb6b6a998b4592cd3738ac76db",
//       "address_person": "",
//       "address_phone": "",
//       "address_address": "",
//       "custom_order_id": "f8c19079-1828-4518-8901-61b69a5ddedd"
//     }
//   }
// }

