package com.yc.eshop.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 余聪
 * @date 2021/1/19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

    private String orderId;

    private String orderName;

    private Integer totalPrice;

    private Date time;

    private Integer amount;

    private Integer state;

    private Integer isRefundApply;

    private String remark;

    private Integer payWay;

    private Integer couponOwnId;

    private Integer userId;

    private Integer addressId;

    private Integer itemId;

}
