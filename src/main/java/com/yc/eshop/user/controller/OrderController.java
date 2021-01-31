package com.yc.eshop.user.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.yc.eshop.common.dto.CreateOrderParam;
import com.yc.eshop.common.dto.PayOrderParam;
import com.yc.eshop.common.dto.RefundApplyParam;
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

    @ApiOperation("用户获取待支付订单信息")
    @GetMapping("/getWaitPayOrderInfo")
    public ApiResponse<?> getWaitPayOrderInfo(Integer userId) {
        return orderService.getWaitPayOrderInfo(userId);
    }

    @ApiOperation("用户获取待支付订单详细信息")
    @GetMapping("/getWaitPayOrderDetailInfo")
    public ApiResponse<?> getWaitPayOrderDetailInfo(String orderId) {
        return orderService.getWaitPayOrderDetailInfo(orderId);
    }

    @ApiOperation("用户取消待支付订单")
    @DeleteMapping("/delOrder/{orderId}")
    public ApiResponse<?> delOrder(@PathVariable String orderId) {
        return orderService.delOrder(orderId);
    }

    @ApiOperation("用户取消待发货订单")
    @DeleteMapping("/delWaitDeliverOrder/{orderId}")
    public ApiResponse<?> delWaitDeliverOrder(@PathVariable String orderId) {
        return orderService.delWaitDeliverOrder(orderId);
    }

    @ApiOperation("用户获取待发货订单信息")
    @GetMapping("/getWaitDeliverOrderInfo")
    public ApiResponse<?> getWaitDeliverOrderInfo(Integer userId) {
        return orderService.getWaitDeliverOrderInfo(userId);
    }

    @ApiOperation("用户获取待发货订单详细信息")
    @GetMapping("/getWaitDeliverOrderDetailInfo/{orderId}")
    public ApiResponse<?> getWaitDeliverOrderDetailInfo(@PathVariable String orderId) {
        return orderService.getWaitDeliverOrderDetailInfo(orderId);
    }

    @ApiOperation("用户获取待收货订单信息")
    @GetMapping("/getWaitReceiveOrderInfo")
    public ApiResponse<?> getWaitReceiveOrderInfo(Integer userId) {
        return orderService.getWaitReceiveOrderInfo(userId);
    }

    @ApiOperation("用户获取待收货订单详细信息")
    @GetMapping("/getWaitReceiveOrderDetailInfo/{orderId}")
    public ApiResponse<?> getWaitReceiveOrderDetailInfo(@PathVariable String orderId) {
        return orderService.getWaitReceiveOrderDetailInfo(orderId);
    }

    @ApiOperation("用户确认收货")
    @GetMapping("/confirmReceive")
    public ApiResponse<?> confirmReceive(String orderId) {
        return orderService.confirmReceive(orderId);
    }

    @ApiOperation("用户获取已完成订单信息")
    @GetMapping("/getFinishOrderInfo")
    public ApiResponse<?> getFinishOrderInfo(Integer userId) {
        return orderService.getFinishOrderInfo(userId);
    }

    @ApiOperation("用户获取已完成订单详细信息")
    @GetMapping("/getFinishOrderDetailInfo/{orderId}")
    public ApiResponse<?> getFinishOrderDetailInfo(@PathVariable String orderId) {
        return orderService.getFinishOrderDetailInfo(orderId);
    }

    @ApiOperation("用户申请退款")
    @PostMapping("/refundApply")
    public ApiResponse<?> refundApply(@RequestBody RefundApplyParam refundApplyParam) {
        return orderService.refundApply(refundApplyParam);
    }

    @ApiOperation("用户获取已退款订单信息")
    @GetMapping("/getRefundOrderInfo")
    public ApiResponse<?> getRefundOrderInfo(Integer userId) {
        return orderService.getRefundOrderInfo(userId);
    }

    @ApiOperation("用户获取已退款订单详细信息")
    @GetMapping("/getRefundOrderDetailInfo/{orderId}")
    public ApiResponse<?> getRefundOrderDetailInfo(@PathVariable String orderId) {
        return orderService.getRefundOrderDetailInfo(orderId);
    }

    @ApiOperation("用户获取订单详细信息")
    @GetMapping("/getOrderDetailInfo/{orderId}")
    public ApiResponse<?> getOrderDetailInfo(@PathVariable String orderId) {
        return orderService.getOrderDetailInfo(orderId);
    }

    @ApiOperation("用户获取所有订单信息")
    @GetMapping("/getAllOrderInfo")
    public ApiResponse<?> getAllOrderInfo(Integer userId) {
        return orderService.getAllOrderInfo(userId);
    }












}
