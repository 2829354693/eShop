package com.yc.eshop.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.eshop.common.dto.*;
import com.yc.eshop.common.entity.Address;
import com.yc.eshop.common.entity.Cart;
import com.yc.eshop.common.entity.User;
import com.yc.eshop.common.entity.UserCoupon;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.response.ResponseCode;
import com.yc.eshop.common.service.RedisService;
import com.yc.eshop.common.util.ShiroUtil;
import com.yc.eshop.common.vo.CouponExVO;
import com.yc.eshop.common.vo.CouponVO;
import com.yc.eshop.common.vo.ConfirmOrderVO;
import com.yc.eshop.common.vo.UserTokenVO;
import com.yc.eshop.user.mapper.AddressMapper;
import com.yc.eshop.user.mapper.UserMapper;
import com.yc.eshop.user.service.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import java.util.stream.Collectors;

/**
 * @author 余聪
 * @date 2020/10/16
 */
@Service
@Transactional(rollbackFor = Exception.class)
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
    public ApiResponse<User> getUserDataExcpPsw(Integer userId) {
        User user = userMapper.selectById(userId);
        if (Objects.nonNull(user) && !"".equals(user.getHeadPic())) {
            user.setHeadPic(pictureNginxHost + user.getHeadPic());
            user.setPassword("");
            user.setSalt("");
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

    @Override
    public ApiResponse<?> getCartData(Integer userId) {
        if (Objects.isNull(userId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        List<Cart> cartData = userMapper.getCartData(userId);
        if (Objects.isNull(cartData)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数错误！");
        }
        List<Cart> cartList = cartData.stream().peek(cart -> {
            cart.setLogo(pictureNginxHost + cart.getLogo());
            cart.setPicture(pictureNginxHost + cart.getPicture());
        }).collect(Collectors.toList());
        return ApiResponse.ok(cartList);
    }

    @Override
    public ApiResponse<Void> removeCart(JSONObject jsonObject) {
        if (Objects.isNull(jsonObject)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        JSONArray jsonArray = jsonObject.getJSONArray("cartIds");
        List<Integer> cartIds = (List<Integer>) JSONArray.toCollection(jsonArray, Integer.class);
        userMapper.deleteCart(cartIds);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<Void> joinCart(JoinCartParam joinCartParam) {
        if (Objects.isNull(joinCartParam)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        Integer userId = joinCartParam.getUserId();
        Integer itemId = joinCartParam.getItemId();
        Integer buyNum = joinCartParam.getBuyNum();
        if (!isUserExist(userId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "用户不存在！");
        }
        Cart cart1 = userMapper.selectCartByUidIid(userId, itemId);
        if (Objects.nonNull(cart1)) {
            cart1.setAmount(buyNum + cart1.getAmount());
            userMapper.updateCartNum(cart1);
            return ApiResponse.ok();
        }
        Cart cart = Cart.builder()
                .userId(userId)
                .itemId(itemId)
                .amount(buyNum)
                .build();
        userMapper.insertCart(cart);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<Void> changeCartNum(Cart cartDTO) {
        if (Objects.isNull(cartDTO)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        if (Objects.isNull(cartDTO.getCartId()) || Objects.isNull(cartDTO.getAmount())) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        userMapper.updateCartNum(cartDTO);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> getAStoreCoupon(UserCoupon userCoupon) {
        if (Objects.isNull(userCoupon) || Objects.isNull(userCoupon.getCouponId()) || Objects.isNull(userCoupon.getUserId())) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        UserCoupon coupon = userMapper.selectByUidCid(userCoupon);
        if (Objects.nonNull(coupon)) {
            return ApiResponse.ok(0);
        }
        userMapper.insertUserCoupon(userCoupon);
        return ApiResponse.ok(1);
    }

    @Override
    public ApiResponse<?> getConfirmOrderData(JSONObject jsonObject) {
        if (Objects.isNull(jsonObject)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        JSONArray jsonArray = jsonObject.getJSONArray("cartIds");
        List<Integer> cartIds = (List<Integer>) JSONArray.toCollection(jsonArray, Integer.class);
        List<ConfirmOrderVO> confirmOrderData = userMapper.getConfirmOrderData(cartIds);
        if (Objects.isNull(confirmOrderData)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        confirmOrderData.forEach(this::changeOrderData);
        return ApiResponse.ok(confirmOrderData);
    }

    @Override
    public ApiResponse<?> getAConfirmOrderData(IidAmountParam iidAmountParam) {
        Integer itemId = iidAmountParam.getItemId();
        Integer amount = iidAmountParam.getAmount();
        ConfirmOrderVO orderData = userMapper.getAConfirmOrderData(itemId);
        if (Objects.isNull(orderData)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "查询结果为空！");
        }
        changeOrderData(orderData);
        orderData.setAmount(amount);
        return ApiResponse.ok(orderData);
    }

    private void changeOrderData(ConfirmOrderVO confirmOrderVO) {
        confirmOrderVO.setPicture(pictureNginxHost + confirmOrderVO.getPicture());
        confirmOrderVO.setLogo(pictureNginxHost + confirmOrderVO.getLogo());
        confirmOrderVO.setCouponOwnId(-1);
        confirmOrderVO.setDiscount(0);
        confirmOrderVO.setSatisfy(0);
        confirmOrderVO.setEndTimeStr("");
        confirmOrderVO.setRemark("");
    }

    @Override
    public ApiResponse<?> getCanUseCoupon(OrderCouponParam orderCouponParam) {
        if (Objects.isNull(orderCouponParam)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        Integer cartId = orderCouponParam.getCartId();
        Integer orderPrice = orderCouponParam.getOrderPrice();
        Integer userId = orderCouponParam.getUserId();
        Integer storeId = userMapper.getStoreIdByCartId(cartId);
        List<CouponVO> canUseCoupons = userMapper.getCanUseCoupon(userId, orderPrice, storeId);
        if (Objects.isNull(canUseCoupons)) {
            return ApiResponse.ok(null);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        canUseCoupons.forEach(canUseCouponVO -> {
            canUseCouponVO.setStartTimeStr(sdf.format(canUseCouponVO.getStartTime()));
            canUseCouponVO.setEndTimeStr(sdf.format(canUseCouponVO.getEndTime()));
        });
        return ApiResponse.ok(canUseCoupons);
    }

    @Override
    public ApiResponse<?> getCanUseCouponFrom2(OrderCouponV2Param orderCouponV2Param) {
        List<CouponVO> couponVOList = userMapper.getCanUseCoupon(orderCouponV2Param.getUserId(), orderCouponV2Param.getOrderPrice(), orderCouponV2Param.getStoreId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        couponVOList.forEach(couponVO -> {
            couponVO.setStartTimeStr(sdf.format(couponVO.getStartTime()));
            couponVO.setEndTimeStr(sdf.format(couponVO.getEndTime()));
        });
        return ApiResponse.ok(couponVOList);
    }

    @Override
    public ApiResponse<?> getCanUseCouponByUid(Integer userId) {
        if (Objects.isNull(userId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        List<CouponExVO> canUseCouponExVOS = userMapper.getCanUseCouponByUid(userId);
        return ApiResponse.ok(date2String(canUseCouponExVOS));
    }

    @Override
    public ApiResponse<?> getNotStartCouponByUid(Integer userId) {
        if (Objects.isNull(userId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        List<CouponExVO> notStartCouponExVOS = userMapper.getNotStartCouponByUid(userId);
        return ApiResponse.ok(date2String(notStartCouponExVOS));
    }

    @Override
    public ApiResponse<?> getEndCouponByUid(Integer userId) {
        if (Objects.isNull(userId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        List<CouponExVO> endCouponExVOS = userMapper.getEndCouponByUid(userId);
        return ApiResponse.ok(date2String(endCouponExVOS));
    }

    @Override
    public ApiResponse<Void> delEndCoupon(Integer couponOwnId) {
        userMapper.delEndCoupon(couponOwnId);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> getCoinByUid(Integer userId) {
        if (Objects.isNull(userId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "用户id为空！");
        }
        Integer coin = userMapper.getCoinByUid(userId);
        return ApiResponse.ok(coin);
    }

    private List<CouponExVO> date2String(List<CouponExVO> couponExVOS) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        couponExVOS.forEach(couponExVO -> {
            couponExVO.setStartTimeStr(sdf.format(couponExVO.getStartTime()));
            couponExVO.setEndTimeStr(sdf.format(couponExVO.getEndTime()));
        });
        return couponExVOS;
    }

    private Boolean isUserExist(Integer userId) {
        User user = userMapper.selectById(userId);
        return Objects.nonNull(user);
    }


























}
