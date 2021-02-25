package com.dwl.service_order.util;

public enum WX {

    APP_ID("wx74862e0dfcf69954"),
    MCH_ID("1558950191"),
    PAY_CLIENT("https://api.mch.weixin.qq.com/pay/unifiedorder"),
    QUERY_CLIENT("https://api.mch.weixin.qq.com/pay/orderquery"),
    KEY("T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"),
    NOTIFY_URL("http://guli.shop/api/order/weixinPay/weixinNotify");

    private String str;

    WX(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
