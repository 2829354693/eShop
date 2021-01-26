package com.yc.eshop.user.service;

import com.alipay.api.AlipayApiException;
import com.yc.eshop.common.dto.CreateOrderParam;
import com.yc.eshop.common.dto.PayOrderParam;
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

}
