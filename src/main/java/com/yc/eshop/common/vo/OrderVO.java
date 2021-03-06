package com.yc.eshop.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO implements Serializable {

    private String orderId;

    private String picture;

    private Date createTime;

    private String createTimeStr;

    private Integer state;

    private String remark;

    private String title;

    private String nickname;




}
