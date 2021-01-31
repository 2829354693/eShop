package com.yc.eshop.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVO extends OrderInfoVO implements Serializable {

    private Integer satisfy;

    private String receiveUsername;

    private String receiveTelephone;

    private String address;

    private String remark;

    private Date createTime;

    private String createTimeStr;

    private Date payTime;

    private String payTimeStr;

    private Date deliverTime;

    private String deliverTimeStr;

    private Date finishTime;

    private String finishTimeStr;

    private Date refundTime;

    private String refundTimeStr;

}
