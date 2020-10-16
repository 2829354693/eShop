package com.yc.eshop.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.eshop.common.entity.User;
import com.yc.eshop.common.response.ApiResponse;

/**
 * @author 余聪
 * @date 2020/10/16
 */
public interface UserService extends IService<User> {

    ApiResponse<Void> register(User userDTO);

}
