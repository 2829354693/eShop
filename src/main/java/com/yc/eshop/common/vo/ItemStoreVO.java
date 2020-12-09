package com.yc.eshop.common.vo;

import com.yc.eshop.common.entity.Item;
import lombok.*;

import java.io.Serializable;

/**
 * @author 余聪
 * @date 2020/12/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemStoreVO extends Item implements Serializable {

    private Integer storeId;

    private String name;

    private String logo;
}
