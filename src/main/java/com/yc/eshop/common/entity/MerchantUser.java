package com.yc.eshop.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("merchant")
public class MerchantUser implements Serializable {

    @TableId(value = "merchant_id", type = IdType.AUTO)
    private Integer merchantId;

    @NotBlank
    @Pattern(regexp = "^1\\d{10}", message = "请输入正确的手机号")
    private String account;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    private String salt;

    private String nickname;

    private String token;

}
