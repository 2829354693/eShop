package com.yc.eshop.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 余聪
 * @date 2020/12/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchItemParam implements Serializable {
    private String itemType;

    private String titleKeywords;

    private Integer minPrice;

    private Integer maxPrice;

}
