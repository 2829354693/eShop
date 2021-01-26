package com.yc.eshop.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.eshop.common.dto.*;
import com.yc.eshop.common.entity.Address;
import com.yc.eshop.common.entity.Cart;
import com.yc.eshop.common.entity.User;
import com.yc.eshop.common.entity.UserCoupon;
import com.yc.eshop.common.response.ApiResponse;
import net.sf.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author 余聪
 * @date 2020/10/16
 */
public interface UserService extends IService<User> {

    ApiResponse<Void> register(User userDTO);

    ApiResponse<User> getUserData(Integer userId);

    ApiResponse<User> getUserDataExcpPsw(Integer userId);

    ApiResponse<?> login(User user) throws InvocationTargetException, IllegalAccessException;

    ApiResponse<Void> logout(HttpServletRequest request);

    ApiResponse<User> changeUserInfo(User userDTO);

    ApiResponse<Void> changeUserPass(PasswordParam passwordParam, HttpServletRequest request);

    ApiResponse<Void> uploadUserHeadPic(MultipartFile headPic, Integer userId) throws IOException;

    ApiResponse<Void> chargeCoin(Integer userId, Integer chargeNum);

    ApiResponse<?> getAddressData(Integer userId);

    ApiResponse<Address> addAddress(Address addressDTO);

    ApiResponse<Void> delAddress(Integer addressId);

    ApiResponse<Void> changeAddressUser(Address addressDTO);

    ApiResponse<Void> changeAddress(Address addressDTO);

    ApiResponse<?> getCartData(Integer userId);

    ApiResponse<Void> removeCart(JSONObject jsonObject);

    ApiResponse<Void> joinCart(JoinCartParam joinCartParam);

    ApiResponse<Void> changeCartNum(Cart cartDTO);

    ApiResponse<?> getAStoreCoupon(UserCoupon userCoupon);

    ApiResponse<?> getConfirmOrderData(JSONObject jsonObject);

    ApiResponse<?> getAConfirmOrderData(IidAmountParam iidAmountParam);

    ApiResponse<?> getCanUseCoupon(OrderCouponParam orderCouponParam);

    ApiResponse<?> getCanUseCouponFrom2(OrderCouponV2Param orderCouponV2Param);

    ApiResponse<?> getCanUseCouponByUid(Integer userId);

    ApiResponse<?> getNotStartCouponByUid(Integer userId);

    ApiResponse<?> getEndCouponByUid(Integer userId);

    ApiResponse<Void> delEndCoupon(Integer couponOwnId);

    ApiResponse<?> getCoinByUid(Integer userId);






}
