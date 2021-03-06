package com.yc.eshop.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchOrderParam implements Serializable {

    private Integer merchantId;

    private Integer pageSize;

    private Integer pageIndex;

    private String orderId;

    private Integer state;

    private String itemTitle;

    private Date[] time;

    private Date startTime;

    private Date endTime;

}
