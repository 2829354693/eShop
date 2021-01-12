package com.yc.eshop.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.eshop.common.entity.Address;
import com.yc.eshop.common.entity.Cart;
import com.yc.eshop.common.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jdbc.repository.query.Modifying;

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


}
