package com.yc.eshop.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 余聪
 * @date 2021/1/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderParam {

    @NotNull
    private Integer userId;
    @NotNull
    private Integer addressId;
    @NotNull
    private List<OrderParam> orders;

}
