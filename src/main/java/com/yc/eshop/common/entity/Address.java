package com.yc.eshop.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author 余聪
 * @date 2020/12/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("address")
public class Address implements Serializable {

    @TableId(value = "address_id", type = IdType.AUTO)
    private Integer addressId;

    private String tag;

    @Size(min = 1, max = 5)
    private String receiveUsername;

    @NotBlank
    @Pattern(regexp = "^1\\d{10}", message = "请输入正确的手机号")
    private String receiveTelephone;

    @NotBlank
    private String address;

    @NotNull
    private Integer userId;

}
