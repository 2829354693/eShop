package com.yc.eshop.common.vo;

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
public class EChartsSingleDataVO implements Serializable {

    private String xAxis;

    private Integer yAxis;

}
