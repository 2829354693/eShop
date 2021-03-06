package com.yc.eshop.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchItemV2Param implements Serializable {

    private Integer merchantId;

    private Integer pageSize;

    private Integer pageIndex;

    private String title;

    private Integer state;
}
