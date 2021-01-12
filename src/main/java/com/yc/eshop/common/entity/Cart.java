package com.yc.eshop.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 余聪
 * @date 2021/1/7
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart implements Serializable {

    private Integer cartId;

    private Integer itemId;

    private Integer userId;

    private Integer storeId;

    private String logo;

    private String name;

    private String picture;

    private String title;

    private Integer price;

    private Integer state;

    private Integer amount;

}
