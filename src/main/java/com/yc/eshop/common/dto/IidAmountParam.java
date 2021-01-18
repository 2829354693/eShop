package com.yc.eshop.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 余聪
 * @date 2021/1/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IidAmountParam {
    @NotNull
    private Integer itemId;
    @NotNull
    @Min(1)
    private Integer amount;
}
