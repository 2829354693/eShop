package com.yc.eshop.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 余聪
 * @date 2021/1/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponVO implements Serializable {
    private Integer couponOwnId;

    private Integer discount;

    private Integer satisfy;

    private Date startTime;

    private String startTimeStr;

    private Date endTime;

    private String endTimeStr;

    private String name;
}
