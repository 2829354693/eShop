package com.yc.eshop.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThreeStatisticsDataVO implements Serializable {

    private Integer orderNum;

    private Integer soldOutItemNum;

    private Integer dealMoney;

}
