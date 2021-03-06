package com.yc.eshop.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundApply implements Serializable {

    private Integer refundId;

    private String orderId;

    private Integer status;

    private String reason;

    private Date insertTime;

    private String insertTimeStr;

}
