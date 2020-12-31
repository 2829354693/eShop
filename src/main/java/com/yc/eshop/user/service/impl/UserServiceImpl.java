package com.yc.eshop.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.eshop.common.dto.PasswordParam;
import com.yc.eshop.common.entity.Address;
import com.yc.eshop.common.entity.User;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.response.ResponseCode;
import com.yc.eshop.common.service.RedisService;
import com.yc.eshop.common.util.ShiroUtil;
import com.yc.eshop.common.vo.UserTokenVO;
import com.yc.eshop.user.mapper.AddressMapper;
import com.yc.eshop.user.mapper.UserMapper;
import com.yc.eshop.user.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author 余聪
 * @date 2020/10/16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${user-token-expire}")
    Integer userTokenExpire;

    @Value("${picture-nginx-host}")
    private String pictureNginxHost;

    @Value("${user-head-pic-save-path}")
    private String userHeadPicSavePath;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AddressMapper addressMapper;

    @Autowired
    RedisService redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<Void> register(User userDTO) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", userDTO.getAccount());
        User userEx = userMapper.selectOne(queryWrapper);
        if (!Objects.isNull(userEx)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "此账号已经注册！");
        }

        String salt = ShiroUtil.createSalt();
        String encryptPwd = ShiroUtil.saltEncrypt(userDTO.getPassword(), salt);

        User user = User.builder()
                .account(userDTO.getAccount())
                .password(encryptPwd)
                .salt(salt)
                .nickname("用户_" + userDTO.getAccount().substring(0, 5))
                .sex("保密")
                .headPic("")
                .coin(100000)
                .build();
        userMapper.insertUser(user);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<User> getUserData(Integer userId) {
        User user = userMapper.selectById(userId);
        if (Objects.nonNull(user) && !"".equals(user.getHeadPic())) {
            user.setHeadPic(pictureNginxHost + user.getHeadPic());
        }
        return ApiResponse.ok(user);
    }

    @Override
    public ApiResponse<?> login(User user) throws InvocationTargetException, IllegalAccessException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", user.getAccount());
        User userEx = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(userEx)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "账号未注册！");
        }
        String encryptPwd = ShiroUtil.saltEncrypt(user.getPassword(), userEx.getSalt());
        if (Objects.equals(encryptPwd, userEx.getPassword())) {
            String token = UUID.randomUUID().toString();
            redisService.setExpire(token, userEx.getUserId().toString(), userTokenExpire);
            UserTokenVO userTokenVO = new UserTokenVO();
            BeanUtils.copyProperties(userTokenVO, userEx);
            userTokenVO.setToken(token);
            return ApiResponse.ok(userTokenVO);
        } else {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "密码错误！");
        }
    }

    @Override
    public ApiResponse<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Boolean delete = redisService.delete(token);
        if (!delete) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "已退出登录！");
        }
        return ApiResponse.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<User> changeUserInfo(User userDTO) {
        userMapper.updateUserInfo(userDTO);
        return getUserData(userDTO.getUserId());
    }

    @Override
    public ApiResponse<Void> changeUserPass(PasswordParam passwordParam, HttpServletRequest request) {
        Integer userId = passwordParam.getUserId();
        String password = passwordParam.getPassword();
        String newPassword = passwordParam.getNewPassword();
        User passSalt = userMapper.getPassSalt(userId);

        if (Objects.isNull(passSalt)) {
            return ApiResponse.failure(ResponseCode.BAD_REQUEST, "未查找到此用户id！");
        }

        if (passSalt.getPassword().equals(ShiroUtil.saltEncrypt(password, passSalt.getSalt()))) {
            userMapper.updatePass(userId, ShiroUtil.saltEncrypt(newPassword, passSalt.getSalt()));
            String token = request.getHeader("Authorization");
            redisService.delete(token);
            return ApiResponse.ok();
        } else {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "原始密码不正确！");
        }
    }

    @Override
    public ApiResponse<Void> uploadUserHeadPic(MultipartFile headPic, Integer userId) throws IOException {
        if (Objects.isNull(headPic) || Objects.isNull(userId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "上传头像失败！图片或用户id为空！");
        }
        String originalFilename = headPic.getOriginalFilename();
        if (Objects.isNull(originalFilename)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "上传头像失败！图片名为空！");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newPicName = df.format(new Date()) + "_" + userId + originalFilename.substring(originalFilename.lastIndexOf("."));
        File newPic = new File(userHeadPicSavePath + newPicName);
        headPic.transferTo(newPic);
        userMapper.updateUserHeadPic(userId, "user_head/" + newPicName);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<Void> chargeCoin(Integer userId, Integer chargeNum) {
        if (Objects.isNull(userId) || Objects.isNull(chargeNum)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        Integer coin = userMapper.getCoinById(userId);
        if (Objects.isNull(coin)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数错误！");
        }
        if (coin + chargeNum > 1000000000) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "余额超出最大值！");
        }
        userMapper.updateCoinById(userId, coin + chargeNum);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> getAddressData(Integer userId) {
        if (Objects.isNull(userId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Address> addressList = addressMapper.selectList(queryWrapper);
        return ApiResponse.ok(addressList, (long) addressList.size());
    }

    @Override
    public ApiResponse<Address> addAddress(Address addressDTO) {
        addressMapper.insert(addressDTO);
        return ApiResponse.ok(addressDTO);
    }

    @Override
    public ApiResponse<Void> delAddress(Integer addressId) {
        addressMapper.deleteById(addressId);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<Void> changeAddressUser(Address addressDTO) {
        if (Objects.isNull(addressDTO)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        Integer addressId = addressDTO.getAddressId();
        String tag = addressDTO.getTag();
        String receiveUsername = addressDTO.getReceiveUsername();
        String receiveTelephone = addressDTO.getReceiveTelephone();
        if (Objects.isNull(addressId) || Objects.isNull(tag) || Objects.isNull(receiveUsername) || Objects.isNull(receiveTelephone)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        addressMapper.updateAddressUser(addressDTO);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<Void> changeAddress(Address addressDTO) {
        if (Objects.isNull(addressDTO)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        Integer addressId = addressDTO.getAddressId();
        String address = addressDTO.getAddress();
        if (Objects.isNull(addressId) || Objects.isNull(address)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        addressMapper.updateAddress(addressDTO);
        return ApiResponse.ok();
    }


}
