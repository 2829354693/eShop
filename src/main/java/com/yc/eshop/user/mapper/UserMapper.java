package com.yc.eshop.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.eshop.common.entity.Cart;
import com.yc.eshop.common.entity.User;
import com.yc.eshop.common.entity.UserCoupon;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.vo.CouponExVO;
import com.yc.eshop.common.vo.CouponVO;
import com.yc.eshop.common.vo.ConfirmOrderVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 余聪
 * @date 2020/10/16
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    void insertUser(User user);

    void updateUserInfo(User user);

    User getPassSalt(Integer userId);

    void updatePass(Integer userId, String newPassword);

    void updateUserHeadPic(Integer userId, String headPic);

    Integer getCoinById(Integer userId);

    void updateCoinById(Integer userId, Integer newCoin);

    List<Cart> getCartData(Integer userId);

    void deleteCart(List<Integer> cartIds);

    void insertCart(Cart cart);

    void updateCartNum(Cart cart);

    void insertUserCoupon(UserCoupon userCoupon);

    UserCoupon selectByUidCid(UserCoupon userCoupon);

    List<ConfirmOrderVO> getConfirmOrderData(List<Integer> cartIds);

    Integer getStoreIdByCartId(Integer cartId);

    List<CouponVO> getCanUseCoupon(Integer userId, Integer orderPrice, Integer storeId);

    List<CouponExVO> getCanUseCouponByUid(Integer userId);

    List<CouponExVO> getNotStartCouponByUid(Integer userId);

    List<CouponExVO> getEndCouponByUid(Integer userId);

    Cart selectCartByUidIid(Integer userId, Integer itemId);

    void delEndCoupon(Integer couponOwnId);















}
