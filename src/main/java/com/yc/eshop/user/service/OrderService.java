package com.yc.eshop.user.service;

import com.yc.eshop.common.dto.CreateOrderParam;
import com.yc.eshop.common.response.ApiResponse;

/**
 * @author 余聪
 * @date 2021/1/19
 */
public interface OrderService {

    ApiResponse<?> createOrder(CreateOrderParam createOrderParam);

}
