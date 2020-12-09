package com.yc.eshop.common.vo;

import com.yc.eshop.common.entity.User;
import lombok.*;

import java.io.Serializable;

/**
 * @author 余聪
 * @date 2020/12/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenVO extends User implements Serializable {

    private String token;
}
