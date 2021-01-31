package com.yc.eshop.common.enums;

/**
 * @author 余聪
 * @date 2021/1/19
 */
public enum OrderStateEnum {

    WAIT_PAY(1),

    WAIT_DELIVER(2),

    WAIT_RECEIVE(3),

    FINISH(4),

    REFUND(5);

    private final Integer orderState;

    OrderStateEnum(Integer orderState) {
        this.orderState = orderState;
    }

    public Integer getOrderState() {
        return orderState;
    }
}
