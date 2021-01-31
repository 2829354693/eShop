package com.yc.eshop.user.service;

import com.alipay.api.AlipayApiException;
import com.yc.eshop.common.dto.CreateOrderParam;
import com.yc.eshop.common.dto.PayOrderParam;
import com.yc.eshop.common.dto.RefundApplyParam;
import com.yc.eshop.common.response.ApiResponse;

import java.util.List;

/**
 * @author 余聪
 * @date 2021/1/19
 */
public interface OrderService {

    ApiResponse<?> createOrder(CreateOrderParam createOrderParam);

    ApiResponse<?> getOrderTotalPrice(PayOrderParam payOrderParam);

    ApiResponse<?> payNow(PayOrderParam payOrderParam);

    ApiResponse<?> getAliPayForm(PayOrderParam payOrderParam) throws AlipayApiException;

    void updateOrderFromAliPay(List<String> orderIds);

    ApiResponse<?> getWaitPayOrderInfo(Integer userId);

    ApiResponse<?> getWaitPayOrderDetailInfo(String orderId);

    ApiResponse<?> delOrder(String orderId);

    ApiResponse<?> delWaitDeliverOrder(String orderId);

    ApiResponse<?> getWaitDeliverOrderInfo(Integer userId);

    ApiResponse<?> getWaitDeliverOrderDetailInfo(String orderId);

    ApiResponse<?> getWaitReceiveOrderInfo(Integer userId);

    ApiResponse<?> getWaitReceiveOrderDetailInfo(String orderId);

    ApiResponse<?> confirmReceive(String orderId);

    ApiResponse<?> getFinishOrderInfo(Integer userId);

    ApiResponse<?> getFinishOrderDetailInfo(String orderId);

    ApiResponse<?> refundApply(RefundApplyParam refundApplyParam);

    ApiResponse<?> getRefundOrderInfo(Integer userId);

    ApiResponse<?> getRefundOrderDetailInfo(String orderId);

    ApiResponse<?> getAllOrderInfo(Integer userId);

    ApiResponse<?> getOrderDetailInfo(String orderId);
}
