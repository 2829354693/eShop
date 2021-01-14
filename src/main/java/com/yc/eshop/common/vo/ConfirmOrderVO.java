package com.yc.eshop.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 余聪
 * @date 2021/1/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmOrderVO implements Serializable {
    private Integer cartId;

    private Integer amount;

    private String title;

    private Integer price;

    private String picture;

    private Integer storeId;

    private String logo;

    private String name;

    private Integer couponOwnId;

    private Integer discount;

    private Integer satisfy;

    private String endTimeStr;

    private String remark;
}
