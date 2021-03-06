package com.yc.eshop.merchant.controller;

import com.yc.eshop.common.dto.PasswordV2Param;
import com.yc.eshop.common.dto.SearchItemV2Param;
import com.yc.eshop.common.dto.SearchOrderParam;
import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.MerchantUser;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.merchant.service.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@Validated
@Api(value = "/merchant", tags = "商家Controller")
@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    MerchantService merchantService;

    @ApiOperation("商家登录")
    @PostMapping("/merchantLogin")
    public ApiResponse<?> merchantLogin(@RequestBody @Validated MerchantUser merchantUser) {
        return merchantService.merchantLogin(merchantUser);
    }

    @ApiOperation("商家退出登录")
    @GetMapping("/merchantLogout")
    public ApiResponse<?> merchantLogout(HttpServletRequest request) {
        return merchantService.merchantLogout(request);
    }

    @ApiOperation("商家获取店铺信息")
    @GetMapping("/getIndexStoreData/{merchantId}")
    public ApiResponse<?> getIndexStoreData(@PathVariable Integer merchantId) {
        return merchantService.getIndexStoreData(merchantId);
    }

    @ApiOperation("商家获取商品列表")
    @PostMapping("/getItemList")
    public ApiResponse<?> getItemList(@RequestBody SearchItemV2Param searchItemV2Param) {
        return merchantService.getItemList(searchItemV2Param);
    }

    @PostMapping("/addItem")
    public ApiResponse<?> addItem(MultipartFile[] pics, @RequestParam("new_des_pic") MultipartFile newDesPic,String title,Integer merchantId,
                                  String type,String description,Integer inventory,Integer price) throws IOException {
        return merchantService.addItem(pics,newDesPic,title,merchantId,type,description,inventory,price);
    }

    @ApiOperation("商家获取单个商品信息")
    @GetMapping("/merchantGetItem/{itemId}")
    public ApiResponse<?> merchantGetItem(@PathVariable Integer itemId) {
        return merchantService.merchantGetItem(itemId);
    }

    @ApiOperation("商家更新单个商品信息")
    @PostMapping("/updateItem")
    public ApiResponse<?> updateItem(@RequestBody Item item) {
        return merchantService.updateItem(item);
    }

    @ApiOperation("下架商品")
    @GetMapping("/offItem/{itemId}")
    public ApiResponse<?> offItem(@PathVariable Integer itemId) {
        return merchantService.offItem(itemId);
    }

    @ApiOperation("上架商品")
    @GetMapping("/onItem/{itemId}")
    public ApiResponse<?> onItem(@PathVariable Integer itemId) {
        return merchantService.onItem(itemId);
    }

    @ApiOperation("获取优惠券列表")
    @PostMapping("/getCouponList")
    public ApiResponse<?> getCouponList(@RequestBody SearchItemV2Param searchItemV2Param) {
        return merchantService.getCouponList(searchItemV2Param);
    }

    @ApiOperation("更改优惠券状态")
    @PostMapping("/changeCouponState")
    public ApiResponse<?> changeCouponState(Integer couponId, Boolean state) {
        return merchantService.changeCouponState(couponId, state);
    }

    @ApiOperation("发放优惠券")
    @PostMapping("/addCoupon")
    public ApiResponse<?> addCoupon(Integer merchantId, Integer discount, Integer satisfy, Date startTime, Date endTime) {
        return merchantService.addCoupon(merchantId, discount,satisfy,startTime,endTime);
    }

    @ApiOperation("获取订单列表")
    @PostMapping("/getOrderList")
    public ApiResponse<?> getOrderList(@RequestBody SearchOrderParam searchOrderParam) {
        return merchantService.getOrderList(searchOrderParam);
    }

    @ApiOperation("商家获取订单详情")
    @GetMapping("/getMerchantOrderDetailInfo/{orderId}")
    public ApiResponse<?> getMerchantOrderDetailInfo(@PathVariable String orderId) {
        return merchantService.getMerchantOrderDetailInfo(orderId);
    }

    @ApiOperation("商家发货")
    @GetMapping("/deliverByOid/{orderId}")
    public ApiResponse<?> deliverByOid(@PathVariable String orderId) {
        return merchantService.deliverByOid(orderId);
    }

    @ApiOperation("商家获取退款申请列表")
    @PostMapping("/getRefundApplyList")
    public ApiResponse<?> getRefundApplyList(@RequestBody SearchOrderParam searchOrderParam) {
        return merchantService.getRefundApplyList(searchOrderParam);
    }

    @ApiOperation("商家获取退款订单详情")
    @GetMapping("/getMerchantRefundOrderDetailInfo/{orderId}")
    public ApiResponse<?> getMerchantRefundOrderDetailInfo(@PathVariable String orderId) {
        return merchantService.getMerchantRefundOrderDetailInfo(orderId);
    }

    @ApiOperation("商家同意退款")
    @GetMapping("/consentRefund/{refundId}")
    public ApiResponse<?> consentRefund(@PathVariable Integer refundId) {
        return merchantService.consentRefund(refundId);
    }

    @ApiOperation("商家拒绝退款")
    @GetMapping("/refuseRefund/{refundId}")
    public ApiResponse<?> refuseRefund(@PathVariable Integer refundId) {
        return merchantService.refuseRefund(refundId);
    }

    @PostMapping("/changeMerchantPassword")
    public ApiResponse<?> changeMerchantPassword(@RequestBody @Validated PasswordV2Param passwordV2Param,HttpServletRequest request) {
        return merchantService.changeMerchantPassword(passwordV2Param, request);
    }

    @PostMapping("/changeMerchantNickname")
    public ApiResponse<?> changeMerchantNickname(@RequestBody MerchantUser merchantUser) {
        return merchantService.changeMerchantNickname(merchantUser);
    }

    @GetMapping("/getMerchantThreeData/{merchantId}")
    public ApiResponse<?> getMerchantThreeData(@PathVariable Integer merchantId) {
        return merchantService.getMerchantThreeData(merchantId);
    }

    @GetMapping("/getEChartsData/{merchantId}")
    public ApiResponse<?> getEChartsData(@PathVariable Integer merchantId) {
        return merchantService.getEChartsData(merchantId);
    }








}
