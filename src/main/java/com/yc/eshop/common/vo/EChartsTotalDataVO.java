package com.yc.eshop.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EChartsTotalDataVO implements Serializable {

    private List<String> yearAndMonth;

    private List<Integer> payNum;

    private List<Integer> refundNum;

}
