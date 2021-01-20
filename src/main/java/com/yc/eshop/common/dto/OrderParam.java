package com.yc.eshop.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 余聪
 * @date 2021/1/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderParam {

    private Integer cartId;

    private Integer itemId;

    private Integer amount;
    @NotNull
    private Integer couponOwnId;

    private String remark;
}
