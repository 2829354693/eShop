package com.yc.eshop.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.eshop.common.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 余聪
 * @date 2020/10/16
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    void insertUser(User user);

    void updateUserInfo(User user);
}
