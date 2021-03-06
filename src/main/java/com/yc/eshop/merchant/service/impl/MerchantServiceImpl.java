package com.yc.eshop.merchant.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yc.eshop.common.dto.PasswordV2Param;
import com.yc.eshop.common.dto.SearchItemV2Param;
import com.yc.eshop.common.dto.SearchOrderParam;
import com.yc.eshop.common.entity.*;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.response.ResponseCode;
import com.yc.eshop.common.service.RedisService;
import com.yc.eshop.common.util.ShiroUtil;
import com.yc.eshop.common.vo.*;
import com.yc.eshop.merchant.mapper.MerchantMapper;
import com.yc.eshop.merchant.service.MerchantService;
import com.yc.eshop.user.mapper.OrderMapper;
import com.yc.eshop.visitor.mapper.IndexMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class MerchantServiceImpl implements MerchantService {

    @Value("${picture-nginx-host}")
    private String pictureNginxHost;

    @Value("${merchant-token-expire}")
    Integer merchantTokenExpire;

    @Value("${item-pic-save-path}")
    String itemPicSavePath;

    @Value("${item-des-pic-save-path}")
    String itemDesPicSavePath;

    @Autowired
    IndexMapper indexMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    MerchantMapper merchantMapper;

    @Autowired
    RedisService redisService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ApiResponse<?> merchantLogin(MerchantUser merchantUser) {
        String account = merchantUser.getAccount();
        String password = merchantUser.getPassword();

        Integer countByAccount = indexMapper.getShopApplyCountByAccount(account);
        if (countByAccount == 0) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "此账号还未提交审核！快去申请入驻吧！");
        }

        Integer shopApplyState = merchantMapper.getShopApplyState(account);
        if (shopApplyState == 1) {
            return ApiResponse.ok("此账号正在审核中！");
        } else if (shopApplyState == 3) {
            return ApiResponse.ok("此账号入驻申请未通过！请更改申请的相关信息！");
        } else {
            MerchantUser mUser = merchantMapper.getMerchantUserByAccount(account);
            String encryptPwd = ShiroUtil.saltEncrypt(password, mUser.getSalt());
            if (Objects.equals(encryptPwd, mUser.getPassword())) {
                String token = UUID.randomUUID().toString();
                redisService.setExpire(token, mUser.getMerchantId().toString(), merchantTokenExpire);
                mUser.setPassword(null);
                mUser.setSalt(null);
                mUser.setToken(token);
                return ApiResponse.ok(mUser);
            } else {
                return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "密码错误！");
            }
        }
    }

    @Override
    public ApiResponse<?> merchantLogout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Boolean delete = redisService.delete(token);
        if (!delete) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "已退出登录！");
        }
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> getIndexStoreData(Integer merchantId) {
        if (Objects.isNull(merchantId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        Store store = merchantMapper.getStoreDataByMid(merchantId);
        store.setLogo(pictureNginxHost + store.getLogo());
        return ApiResponse.ok(store);
    }

    @Override
    public ApiResponse<?> getItemList(SearchItemV2Param searchItemV2Param) {
        if (Objects.isNull(searchItemV2Param)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }

        PageHelper.startPage(searchItemV2Param.getPageIndex(), searchItemV2Param.getPageSize());
        List<Item> itemList = merchantMapper.getItemList(searchItemV2Param);
        for (Item item : itemList) {
            item.setPicture(pictureNginxHost + item.getPicture());
            item.setUpdateTimeStr(sdf.format(item.getUpdateTime()));
        }
        PageInfo<Item> pageInfo = new PageInfo<>(itemList);
        return ApiResponse.ok(pageInfo);
    }

    @Override
    public ApiResponse<?> addItem(MultipartFile[] pics, MultipartFile newDesPic, String title, Integer merchantId, String type, String description, Integer inventory, Integer price) throws IOException {
        if (pics.length == 0) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE,"至少上传一张商品图片！");
        }

        String firstPicture = null;
        StringBuilder pictureStr = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSS");
        for (int i = 0; i < pics.length; i++) {
            String originalFilename = pics[i].getOriginalFilename();
            if (Objects.isNull(originalFilename) || originalFilename.length() == 0) {
                return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "商品图片名称不能为空！");
            }
            String newPicName = df.format(new Date()) + "_item_" + i + originalFilename.substring(originalFilename.lastIndexOf("."));
            if (i == 0) {
                firstPicture = "item_pic/" + newPicName;
            }
            File itemPic = new File(itemPicSavePath + newPicName);
            pics[i].transferTo(itemPic);
            pictureStr.append("item_pic/").append(newPicName).append(",");
        }
        String pictures = pictureStr.deleteCharAt(pictureStr.length() - 1).toString();
        String originalFilename = newDesPic.getOriginalFilename();
        if (Objects.isNull(originalFilename) || originalFilename.length() == 0) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "商品图片名称不能为空！");
        }
        String newPicName = df.format(new Date()) + "_item_des_" + merchantId + originalFilename.substring(originalFilename.lastIndexOf("."));
        File itemDesPic = new File(itemDesPicSavePath + newPicName);
        newDesPic.transferTo(itemDesPic);

        Store store = merchantMapper.getStoreDataByMid(merchantId);
        Item newItem = Item.builder()
                .description(description)
                .desPic("item_des_pic/"+newPicName)
                .inventory(inventory)
                .picture(firstPicture)
                .pictures(pictures)
                .price(price)
                .sales(0)
                .storeId(store.getStoreId())
                .title(title)
                .type(type)
                .state(1)
                .build();
        merchantMapper.insertNewItem(newItem);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> merchantGetItem(Integer itemId) {
        if (Objects.isNull(itemId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }

        Item item = merchantMapper.getItemByIid(itemId);
        return ApiResponse.ok(item);
    }

    @Override
    public ApiResponse<?> updateItem(Item item) {
        if (Objects.isNull(item)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }

        merchantMapper.updateItem(item);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> offItem(Integer itemId) {
        if (Objects.isNull(itemId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }

        merchantMapper.updateItemStateOff(itemId);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> onItem(Integer itemId) {
        if (Objects.isNull(itemId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }

        merchantMapper.updateItemStateOn(itemId);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> getCouponList(SearchItemV2Param searchItemV2Param) {
        PageHelper.startPage(searchItemV2Param.getPageIndex(), searchItemV2Param.getPageSize());
        List<StoreCoupon> couponList = merchantMapper.getCouponList(searchItemV2Param.getMerchantId());
        for (StoreCoupon coupon : couponList) {
            coupon.setStartTimeStr(sdf.format(coupon.getStartTime()));
            coupon.setEndTimeStr(sdf.format(coupon.getEndTime()));
            coupon.setInsertTimeStr(sdf.format(coupon.getInsertTime()));
        }
        PageInfo<StoreCoupon> pageInfo = new PageInfo<>(couponList);
        return ApiResponse.ok(pageInfo);
    }

    @Override
    public ApiResponse<?> changeCouponState(Integer couponId, Boolean state) {
        merchantMapper.updateCouponState(couponId, state);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> addCoupon(Integer merchantId, Integer discount, Integer satisfy, Date startTime, Date endTime) {
        Store store = merchantMapper.getStoreDataByMid(merchantId);
        StoreCoupon storeCoupon = StoreCoupon.builder()
                .storeId(store.getStoreId())
                .discount(discount)
                .satisfy(satisfy)
                .startTime(startTime)
                .endTime(endTime)
                .build();
        merchantMapper.insertStoreCoupon(storeCoupon);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> getOrderList(SearchOrderParam searchOrderParam) {
        if (Objects.nonNull(searchOrderParam.getTime()) && searchOrderParam.getTime().length > 0) {
            searchOrderParam.setStartTime(searchOrderParam.getTime()[0]);
            searchOrderParam.setEndTime(searchOrderParam.getTime()[1]);
        }
        PageHelper.startPage(searchOrderParam.getPageIndex(), searchOrderParam.getPageSize());
        List<OrderVO> orderList = merchantMapper.getOrderList(searchOrderParam);
        for (OrderVO orderVO : orderList) {
            orderVO.setCreateTimeStr(sdf.format(orderVO.getCreateTime()));
            orderVO.setPicture(pictureNginxHost + orderVO.getPicture());
        }
        PageInfo<OrderVO> pageInfo = new PageInfo<>(orderList);
        return ApiResponse.ok(pageInfo);
    }

    @Override
    public ApiResponse<?> getMerchantOrderDetailInfo(String orderId) {
        if (Objects.isNull(orderId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        OrderDetailVO order = orderMapper.getRefundOrderDetailInfo(orderId);
        handlePicPathAndDate(order);
        if (Objects.nonNull(order.getPayTime())) {
            order.setPayTimeStr(sdf.format(order.getPayTime()));
        }
        if (Objects.nonNull(order.getDeliverTime())) {
            order.setDeliverTimeStr(sdf.format(order.getDeliverTime()));
        }
        if (Objects.nonNull(order.getFinishTime())) {
            order.setFinishTimeStr(sdf.format(order.getFinishTime()));
        }
        if (Objects.nonNull(order.getRefundTime())) {
            order.setRefundTimeStr(sdf.format(order.getRefundTime()));
        }
        return ApiResponse.ok(order);
    }

    @Override
    public ApiResponse<?> deliverByOid(String orderId) {
        if (Objects.isNull(orderId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }

        merchantMapper.deliverByOid(orderId);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> getRefundApplyList(SearchOrderParam searchOrderParam) {
        if (Objects.nonNull(searchOrderParam.getTime()) && searchOrderParam.getTime().length > 0) {
            searchOrderParam.setStartTime(searchOrderParam.getTime()[0]);
            searchOrderParam.setEndTime(searchOrderParam.getTime()[1]);
        }
        PageHelper.startPage(searchOrderParam.getPageIndex(), searchOrderParam.getPageSize());
        List<RefundApply> applyList = merchantMapper.getRefundApplyList(searchOrderParam);
        for (RefundApply refundApply : applyList) {
            refundApply.setInsertTimeStr(sdf.format(refundApply.getInsertTime()));
        }
        PageInfo<RefundApply> pageInfo = new PageInfo<>(applyList);
        return ApiResponse.ok(pageInfo);
    }

    @Override
    public ApiResponse<?> getMerchantRefundOrderDetailInfo(String orderId) {
        if (Objects.isNull(orderId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        OrderDetailVO order = orderMapper.getRefundOrderDetailInfo(orderId);
        handlePicPathAndDate(order);
        order.setPayTimeStr(sdf.format(order.getPayTime()));
        order.setDeliverTimeStr(sdf.format(order.getDeliverTime()));
        order.setFinishTimeStr(sdf.format(order.getFinishTime()));
        return ApiResponse.ok(order);
    }

    @Override
    public ApiResponse<?> consentRefund(Integer refundId) {
        if (Objects.isNull(refundId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        merchantMapper.updateRefundApplyStatus(refundId, 1);
        merchantMapper.updateOrderRefundConsent(refundId);
        Order order = merchantMapper.getUserIdTotalPriceByRefundId(refundId);
        merchantMapper.updateUserCoin(order.getUserId(), order.getTotalPrice());
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> refuseRefund(Integer refundId) {
        if (Objects.isNull(refundId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        merchantMapper.updateRefundApplyStatus(refundId, 2);
        merchantMapper.updateOrderRefundRefuse(refundId);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> changeMerchantPassword(PasswordV2Param passwordV2Param, HttpServletRequest request) {
        Integer merchantId = passwordV2Param.getMerchantId();
        String password = passwordV2Param.getPassword();
        String newPassword = passwordV2Param.getNewPassword();
        MerchantUser mUser = merchantMapper.getSaltByMid(merchantId);
        if (Objects.isNull(mUser)) {
            return ApiResponse.failure(ResponseCode.BAD_REQUEST, "未查找到此商家id！");
        }
        if (Objects.equals(mUser.getPassword(), ShiroUtil.saltEncrypt(password, mUser.getSalt()))) {
            merchantMapper.updateMerchantPassword(merchantId, ShiroUtil.saltEncrypt(newPassword,mUser.getSalt()));
            String token = request.getHeader("Authorization");
            redisService.delete(token);
            return ApiResponse.ok();
        } else {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "原始密码不正确！");
        }


    }

    @Override
    public ApiResponse<?> changeMerchantNickname(MerchantUser merchantUser) {
        if (Objects.isNull(merchantUser)) {
            return ApiResponse.failure(ResponseCode.BAD_REQUEST, "参数为空！");
        }
        merchantMapper.updateMerchantNickname(merchantUser);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> getMerchantThreeData(Integer merchantId) {
        if (Objects.isNull(merchantId)) {
            return ApiResponse.failure(ResponseCode.BAD_REQUEST, "参数为空！");
        }
        Integer orderNum = merchantMapper.getStoreTotalOrderNum(merchantId);
        Integer soldOutItemNum = merchantMapper.getStoreOrderTotalItemNum(merchantId);
        Integer dealMoney = merchantMapper.getDealMoney(merchantId);
        if (Objects.isNull(dealMoney)) {
            dealMoney = 0;
        }
        ThreeStatisticsDataVO threeStatisticsDataVO = ThreeStatisticsDataVO.builder()
                .orderNum(orderNum)
                .soldOutItemNum(soldOutItemNum)
                .dealMoney(dealMoney)
                .build();
        return ApiResponse.ok(threeStatisticsDataVO);
    }

    @Override
    public ApiResponse<?> getEChartsData(Integer merchantId) {
        if (Objects.isNull(merchantId)) {
            return ApiResponse.failure(ResponseCode.BAD_REQUEST, "参数为空！");
        }
        List<EChartsSingleDataVO> payNumEChartsData = merchantMapper.getPayNumEChartsData(merchantId);
        List<EChartsSingleDataVO> refundEChartsData = merchantMapper.getRefundEChartsData(merchantId);
        List<String> yearAndMonth = payNumEChartsData.stream().map(EChartsSingleDataVO::getXAxis).collect(Collectors.toList());
        List<Integer> payNum = payNumEChartsData.stream().map(EChartsSingleDataVO::getYAxis).collect(Collectors.toList());
        List<Integer> refundNum = refundEChartsData.stream().map(EChartsSingleDataVO::getYAxis).collect(Collectors.toList());
        EChartsTotalDataVO eChartsTotalDataVO = EChartsTotalDataVO.builder()
                .yearAndMonth(yearAndMonth)
                .payNum(payNum)
                .refundNum(refundNum)
                .build();
        return ApiResponse.ok(eChartsTotalDataVO);
    }


    private void handlePicPathAndDate(OrderDetailVO order) {
        order.setPicture(pictureNginxHost + order.getPicture());
        order.setLogo(pictureNginxHost + order.getLogo());
        order.setCreateTimeStr(sdf.format(order.getCreateTime()));
        if (Objects.isNull(order.getDiscount())) {
            order.setDiscount(0);
        }
    }
}
