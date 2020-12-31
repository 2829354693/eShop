package com.yc.eshop.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 余聪
 * @date 2020/12/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordParam {

    @NotNull
    private Integer userId;

    @NotBlank
    @Size(min = 3, max = 20)
    private String password;

    @NotBlank
    @Size(min = 3, max = 20)
    private String newPassword;

}
