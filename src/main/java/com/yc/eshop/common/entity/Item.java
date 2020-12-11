package com.yc.eshop.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author 余聪
 * @date 2020/12/7
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("item")
public class Item implements Serializable {

    @TableId(value = "item_id", type = IdType.AUTO)
    private Integer itemId;

    @NotBlank(message = "商品标题不能为空")
    @Size(min = 1, max = 100)
    private String title;

    @NotBlank(message = "商品类型不能为空")
    private String type;

    private String picture;

    private String pictures;

    @NotNull(message = "商品库存不能为空")
    private Integer inventory;

    private String description;

    private String desPic;

    private Integer sales;

    @NotNull(message = "商品价格不能为空")
    private Integer price;

    private Integer state;

    private Integer storeId;

}
