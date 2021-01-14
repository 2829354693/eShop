package com.yc.eshop.user.controller;

import com.yc.eshop.common.dto.JoinCartParam;
import com.yc.eshop.common.dto.OrderCouponParam;
import com.yc.eshop.common.dto.PasswordParam;
import com.yc.eshop.common.entity.Address;
import com.yc.eshop.common.entity.Cart;
import com.yc.eshop.common.entity.User;
import com.yc.eshop.common.entity.UserCoupon;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author 余聪
 * @date 2020/10/16
 */
@Validated
@Api(value = "/user", tags = "用户Controller")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation("用户登录验证")
    @PostMapping("/login")
    public ApiResponse<?> login(@Validated @RequestBody User userDTO) throws InvocationTargetException, IllegalAccessException {
        return userService.login(userDTO);
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ApiResponse<Void> register(@Validated @RequestBody User userDTO) {
        return userService.register(userDTO);
    }

    @ApiOperation("用户注销")
    @GetMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        return userService.logout(request);
    }

    @ApiOperation("获取一个用户信息")
    @GetMapping("/getUserData")
    public ApiResponse<User> getUserData(Integer userId) {
        return userService.getUserData(userId);
    }

    @ApiOperation("获取一个用户信息除去密码")
    @GetMapping("/getUserDataExcpPsw")
    public ApiResponse<User> getUserDataExcpPsw(Integer userId) {
        return userService.getUserDataExcpPsw(userId);
    }

    @ApiOperation("更改用户昵称或性别")
    @PostMapping("/changeUserInfo")
    public ApiResponse<User> changeUserInfo(@RequestBody User userDTO) {
        return userService.changeUserInfo(userDTO);
    }

    @ApiOperation("更改用户密码")
    @PostMapping("/changeUserPass")
    public ApiResponse<Void> changeUserPass(@RequestBody @Validated PasswordParam passwordParam, HttpServletRequest request) {
        return userService.changeUserPass(passwordParam, request);
    }

    @ApiIgnore
    @PostMapping("/headUpload")
    public ApiResponse<Void> uploadUserHeadPic(MultipartFile headPicFile, Integer userId) throws IOException {
        return userService.uploadUserHeadPic(headPicFile, userId);
    }

    @ApiOperation("用户充值余额")
    @PostMapping("/chargeCoin")
    public ApiResponse<Void> chargeCoin(Integer userId, Integer chargeNum) {
        return userService.chargeCoin(userId, chargeNum);
    }

    @ApiOperation("获取用户地址列表")
    @GetMapping("/getAddressData")
    public ApiResponse<?> getAddressData(Integer userId) {
        return userService.getAddressData(userId);
    }

    @ApiOperation("添加地址")
    @PostMapping("/addAddress")
    public ApiResponse<Address> addAddress(@RequestBody @Validated Address addressDTO) {
        return userService.addAddress(addressDTO);
    }

    @ApiOperation("删除地址")
    @GetMapping("/delAddress")
    public ApiResponse<Void> delAddress(Integer addressId) {
        return userService.delAddress(addressId);
    }

    @ApiOperation("更改地址的收货人信息")
    @PostMapping("/changeAddressUser")
    public ApiResponse<Void> changeAddressUser(@RequestBody Address addressDTO) {
        return userService.changeAddressUser(addressDTO);
    }

    @ApiOperation("更改收货地址信息")
    @PostMapping("/changeAddress")
    public ApiResponse<Void> changeAddress(@RequestBody Address addressDTO) {
        return userService.changeAddress(addressDTO);
    }

    @ApiOperation("获取用户购物车信息")
    @GetMapping("/getCartData")
    public ApiResponse<?> getCartData(Integer userId) {
        return userService.getCartData(userId);
    }

    @ApiOperation("移除选中的购物车商品")
    @PostMapping("/removeCart")
    public ApiResponse<Void> removeCart(@RequestBody JSONObject jsonObject) {
        return userService.removeCart(jsonObject);
    }

    @ApiOperation("加入购物车")
    @PostMapping("/joinCart")
    public ApiResponse<Void> joinCart(@RequestBody @Validated JoinCartParam joinCartParam) {
        return userService.joinCart(joinCartParam);
    }

    @ApiOperation("改变购物车商品的数量")
    @PostMapping("/changeCartNum")
    public ApiResponse<Void> changeCartNum(@RequestBody Cart cartDTO) {
        return userService.changeCartNum(cartDTO);
    }

    @ApiOperation("用户领取一张店铺优惠券")
    @PostMapping("/getAStoreCoupon")
    public ApiResponse<?> getAStoreCoupon(@RequestBody UserCoupon userCouponDTO) {
        return userService.getAStoreCoupon(userCouponDTO);
    }

    @ApiOperation("根据购物车id获取下单确认信息")
    @PostMapping("/getConfirmOrderData")
    public ApiResponse<?> getConfirmOrderData(@RequestBody JSONObject jsonObject) {
        return userService.getConfirmOrderData(jsonObject);
    }

    @ApiOperation("确认订单时获取可用优惠券")
    @PostMapping("/getCanUseCoupon")
    public ApiResponse<?> getCanUseCoupon(@RequestBody @Validated OrderCouponParam orderCouponParam) {
        return userService.getCanUseCoupon(orderCouponParam);
    }

    @ApiOperation("用户中心获取可用优惠券")
    @GetMapping("/getCanUseCouponByUid")
    public ApiResponse<?> getCanUseCouponByUid(Integer userId) {
        return userService.getCanUseCouponByUid(userId);
    }

    @ApiOperation("用户中心获取待生效优惠券")
    @GetMapping("/getNotStartCouponByUid")
    public ApiResponse<?> getNotStartCouponByUid(Integer userId) {
        return userService.getNotStartCouponByUid(userId);
    }

    @ApiOperation("用户中心获取过期优惠券")
    @GetMapping("/getEndCouponByUid")
    public ApiResponse<?> getEndCouponByUid(Integer userId) {
        return userService.getEndCouponByUid(userId);
    }

    @ApiOperation("用户删除已过期优惠券")
    @GetMapping("/delEndCoupon")
    public ApiResponse<Void> delEndCoupon(Integer couponOwnId) {
        return userService.delEndCoupon(couponOwnId);
    }



















}
