package com.yc.eshop.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 余聪
 * @date 2021/1/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCouponV2Param {
    @NotNull
    private Integer userId;
    @NotNull
    private Integer orderPrice;
    @NotNull
    private Integer storeId;
}
