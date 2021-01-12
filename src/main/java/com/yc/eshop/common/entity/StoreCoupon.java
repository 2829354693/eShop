package com.yc.eshop.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 余聪
 * @date 2021/1/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreCoupon implements Serializable {
    private Integer couponId;

    private Integer discount;

    private Integer satisfy;

    private Date startTime;

    private String startTimeStr;

    private Date endTime;

    private String endTimeStr;

    private String state;

    private Integer storeId;

}
