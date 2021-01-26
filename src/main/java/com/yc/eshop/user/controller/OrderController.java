package com.yc.eshop.user.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.yc.eshop.common.dto.CreateOrderParam;
import com.yc.eshop.common.dto.PayOrderParam;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.user.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    @ApiOperation("获取订单总价")
    @PostMapping("/getOrderTotalPrice")
    public ApiResponse<?> getOrderTotalPrice(@RequestBody PayOrderParam payOrderParam) {
        return orderService.getOrderTotalPrice(payOrderParam);
    }

    @ApiOperation("账户余额支付")
    @PostMapping("/payNow")
    public ApiResponse<?> payNow(@RequestBody PayOrderParam payOrderParam) {
        return orderService.payNow(payOrderParam);
    }

    @ApiOperation("生成支付宝付款页面")
    @PostMapping("/getAliPayForm")
    public ApiResponse<?> getAliPayForm(@RequestBody PayOrderParam payOrderParam) throws AlipayApiException {
        return orderService.getAliPayForm(payOrderParam);
    }




























}
