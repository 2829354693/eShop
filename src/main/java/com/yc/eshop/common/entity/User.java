package com.yc.eshop.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author 余聪
 * @date 2020/10/16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @NotBlank
    @Pattern(regexp = "^1\\d{10}", message = "请输入正确的手机号")
    private String account;

    @NotBlank
    @Size(min = 3, max = 20)
    private String password;

    private String nickname;

    private String sex;

    private String headPic;

    private Integer coin;

}
