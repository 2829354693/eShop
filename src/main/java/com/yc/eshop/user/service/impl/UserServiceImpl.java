package com.yc.eshop.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.eshop.common.entity.User;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.user.mapper.UserMapper;
import com.yc.eshop.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 余聪
 * @date 2020/10/16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public ApiResponse<Void> register(User userDTO) {
        User user = User.builder()
                .account(userDTO.getAccount())
                .password(userDTO.getPassword())
                .nickname(userDTO.getAccount())
                .sex("无")
                .headPic("")
                .coin(100000)
                .build();
        userMapper.insertUser(user);
        return ApiResponse.ok();
    }
}
