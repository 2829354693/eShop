package com.yc.eshop.common.enums;

public enum RefundEnum {

    WAIT_CHECK(0),

    PASS(1),

    REJECT(2);

    private Integer refundStatus;

    RefundEnum(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }
}
