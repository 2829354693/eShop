package com.yc.eshop.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoVO implements Serializable {

    private String orderId;

    private Integer itemId;

    private String title;

    private Integer price;

    private String picture;

    private Integer itemState;

    private Integer storeId;

    private String logo;

    private String name;

    private Integer state;

    private Integer amount;

    private Integer discount;

    private Integer isComment;

    private Integer isRefundApply;

}
