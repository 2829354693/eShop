package com.yc.eshop.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderParam implements Serializable {

    private List<String> orderIds;

    private Integer userId;

    private Integer totalPrice;

}
