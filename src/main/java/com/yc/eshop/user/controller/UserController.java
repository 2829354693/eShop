package com.yc.eshop.user.controller;

import com.yc.eshop.common.entity.User;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

/**
 * @author 余聪
 * @date 2020/10/16
 */
@Validated
@Api(value = "/user", tags = "用户controller")
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

    @ApiOperation("获取一个账号")
    @GetMapping("/getOne")
    public ApiResponse<User> getOne(Integer id) {
        return userService.getOne(id);
    }


}
