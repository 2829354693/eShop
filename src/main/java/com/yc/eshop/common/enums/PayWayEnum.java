package com.yc.eshop.common.enums;

public enum PayWayEnum {

    COIN_PAY(1),

    ALIPAY(2);

    private final Integer payWay;

    private PayWayEnum(Integer payWay) {
        this.payWay = payWay;
    }

    public Integer getPayWay() {
        return payWay;
    }
}
