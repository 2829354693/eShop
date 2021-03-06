package com.yc.eshop.admin.mapper;

import com.yc.eshop.common.entity.MerchantUser;
import com.yc.eshop.common.entity.ShopApply;
import com.yc.eshop.common.entity.Store;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {

    List<ShopApply> getShopApplyList();

    void updateShopApplyState(Integer state, Integer applyId);

    String getApplyAccountById(Integer id);

    void insertMerchantUser(MerchantUser merchantUser);

    ShopApply getShopApplyById(Integer id);

    void insertStore(Store store);
}
