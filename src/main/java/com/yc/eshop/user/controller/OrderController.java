package com.yc.eshop.user.controller;

import com.yc.eshop.common.dto.CreateOrderParam;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.user.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 余聪
 * @date 2021/1/19
 */
@Validated
@Api(value = "/order", tags = "订单Controller")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @ApiOperation("创建订单")
    @PostMapping("/createOrder")
    public ApiResponse<?> createOrder(@RequestBody CreateOrderParam createOrderParam) {
        return orderService.createOrder(createOrderParam);
    }

































}
