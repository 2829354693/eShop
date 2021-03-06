package com.yc.eshop.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopApply implements Serializable {

    private Integer shopApplyId;

    private String applyAccount;

    private String applyShopName;

    private MultipartFile logoFile;

    private String applyShopLogo;

    private String applyShopType;

    private Date applyTime;

    private String applyTimeStr;

    private Date handleTime;

    private String handleTimeStr;

    private Integer state;


}
