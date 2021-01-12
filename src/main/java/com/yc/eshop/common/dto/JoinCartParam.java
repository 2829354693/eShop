package com.yc.eshop.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 余聪
 * @date 2021/1/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinCartParam {
    @NotNull
    private Integer itemId;
    @NotNull
    @Min(value = 1)
    @Max(value = 999999)
    private Integer buyNum;
    @NotNull
    private Integer userId;
}
