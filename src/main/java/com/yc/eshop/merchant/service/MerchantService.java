package com.yc.eshop.merchant.service;

import com.yc.eshop.common.dto.PasswordV2Param;
import com.yc.eshop.common.dto.SearchItemV2Param;
import com.yc.eshop.common.dto.SearchOrderParam;
import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.MerchantUser;
import com.yc.eshop.common.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

public interface MerchantService {
    ApiResponse<?> merchantLogin(MerchantUser merchantUser);

    ApiResponse<?> merchantLogout(HttpServletRequest request);

    ApiResponse<?> getIndexStoreData(Integer merchantId);

    ApiResponse<?> getItemList(SearchItemV2Param searchItemV2Param);

    ApiResponse<?> addItem(MultipartFile[] pics, MultipartFile newDesPic, String title, Integer merchantId, String type, String description, Integer inventory, Integer price) throws IOException;

    ApiResponse<?> merchantGetItem(Integer itemId);

    ApiResponse<?> updateItem(Item item);

    ApiResponse<?> offItem(Integer itemId);

    ApiResponse<?> onItem(Integer itemId);

    ApiResponse<?> getCouponList(SearchItemV2Param searchItemV2Param);

    ApiResponse<?> changeCouponState(Integer couponId, Boolean state);

    ApiResponse<?> addCoupon(Integer merchantId, Integer discount, Integer satisfy, Date startTime, Date endTime);

    ApiResponse<?> getOrderList(SearchOrderParam searchOrderParam);

    ApiResponse<?> getMerchantOrderDetailInfo(String orderId);

    ApiResponse<?> deliverByOid(String orderId);

    ApiResponse<?> getRefundApplyList(SearchOrderParam searchOrderParam);

    ApiResponse<?> getMerchantRefundOrderDetailInfo(String orderId);

    ApiResponse<?> consentRefund(Integer refundId);

    ApiResponse<?> refuseRefund(Integer refundId);

    ApiResponse<?> changeMerchantPassword(PasswordV2Param passwordV2Param, HttpServletRequest request);

    ApiResponse<?> changeMerchantNickname(MerchantUser merchantUser);

    ApiResponse<?> getMerchantThreeData(Integer merchantId);

    ApiResponse<?> getEChartsData(Integer merchantId);
}
