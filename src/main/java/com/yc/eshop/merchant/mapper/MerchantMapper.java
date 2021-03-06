package com.yc.eshop.merchant.mapper;

import com.yc.eshop.common.dto.SearchItemV2Param;
import com.yc.eshop.common.dto.SearchOrderParam;
import com.yc.eshop.common.entity.*;
import com.yc.eshop.common.vo.EChartsSingleDataVO;
import com.yc.eshop.common.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MerchantMapper {

    Integer getShopApplyState(String account);

    MerchantUser getMerchantUserByAccount(String account);

    Store getStoreDataByMid(Integer merchantId);

    List<Item> getItemList(SearchItemV2Param searchItemV2Param);

    void insertNewItem(Item item);

    Item getItemByIid(Integer itemId);

    void updateItem(Item item);

    void updateItemStateOff(Integer itemId);

    void updateItemStateOn(Integer itemId);

    List<StoreCoupon> getCouponList(Integer merchantId);

    void updateCouponState(Integer couponId, Boolean state);

    void insertStoreCoupon(StoreCoupon storeCoupon);

    List<OrderVO> getOrderList(SearchOrderParam searchOrderParam);

    void deliverByOid(String orderId);

    List<RefundApply> getRefundApplyList(SearchOrderParam searchOrderParam);

    void updateRefundApplyStatus(Integer refundId, Integer status);

    void updateOrderRefundConsent(Integer refundId);

    Order getUserIdTotalPriceByRefundId(Integer refundId);

    void updateUserCoin(Integer userId,Integer price);

    void updateOrderRefundRefuse(Integer refundId);

    MerchantUser getSaltByMid(Integer mId);

    void updateMerchantPassword(Integer merchantId, String pass);

    void updateMerchantNickname(MerchantUser merchant);

    Integer getStoreTotalOrderNum(Integer merchantId);

    Integer getStoreOrderTotalItemNum(Integer merchantId);

    Integer getDealMoney(Integer merchantId);

    List<EChartsSingleDataVO> getPayNumEChartsData(Integer merchantId);

    List<EChartsSingleDataVO> getRefundEChartsData(Integer merchantId);



}
