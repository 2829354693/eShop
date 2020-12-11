package com.yc.eshop.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 余聪
 * @date 2020/12/9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("store")
public class Store implements Serializable {
    @TableId(value = "store_id", type = IdType.AUTO)
    private Integer storeId;

    private String name;

    private String logo;

    private Integer state;

    private Integer merchantId;

    @TableField(exist = false)
    private Integer totalSales;

}
