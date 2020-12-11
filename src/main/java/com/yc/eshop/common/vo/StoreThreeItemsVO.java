package com.yc.eshop.common.vo;

import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author 余聪
 * @date 2020/12/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreThreeItemsVO extends Store implements Serializable {

    private List<Item> threeHotItems;
}
