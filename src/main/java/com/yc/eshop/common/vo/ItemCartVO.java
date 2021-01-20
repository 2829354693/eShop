package com.yc.eshop.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 余聪
 * @date 2021/1/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCartVO {

    private Integer itemId;

    private Integer price;

    private String title;

    private Integer amount;

}
