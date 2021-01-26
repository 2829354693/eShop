package com.yc.eshop.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 余聪
 * @date 2021/1/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon implements Serializable {
    private Integer couponOwnId;

    private Integer couponId;

    private Integer isUse;

    private Integer userId;

}
