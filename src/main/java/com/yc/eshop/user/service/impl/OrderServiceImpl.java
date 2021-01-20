package com.yc.eshop.user.service.impl;

import com.yc.eshop.common.dto.CreateOrderParam;
import com.yc.eshop.common.dto.OrderParam;
import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.Order;
import com.yc.eshop.common.enums.OrderStateEnum;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.response.ResponseCode;
import com.yc.eshop.common.util.UUIDUtils;
import com.yc.eshop.common.vo.ItemCartVO;
import com.yc.eshop.user.mapper.OrderMapper;
import com.yc.eshop.user.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 余聪
 * @date 2021/1/19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Override
    public ApiResponse<?> createOrder(CreateOrderParam createOrderParam) {
        Integer userId = createOrderParam.getUserId();
        Integer addressId = createOrderParam.getAddressId();
        List<OrderParam> orderParams = createOrderParam.getOrders();
        List<String> orderIds = new ArrayList<>();
        if (orderParams.size() == 1) {
            OrderParam orderParam = orderParams.get(0);
            String orderId = UUIDUtils.getOrderId();
            orderIds.add(orderId);
            Item item = orderMapper.getItemByIid(orderParam.getItemId());
            int totalPrice;
            if (orderParam.getCouponOwnId() == -1) {
                totalPrice = item.getPrice() * orderParam.getAmount();
            } else {
                Integer discount = orderMapper.getDiscountByCouponOwnId(orderParam.getCouponOwnId());
                totalPrice = item.getPrice() * orderParam.getAmount() - discount;
            }
            Order order = Order.builder()
                    .orderId(orderId)
                    .orderName(item.getTitle())
                    .addressId(addressId)
                    .amount(orderParam.getAmount())
                    .couponOwnId(orderParam.getCouponOwnId())
                    .itemId(orderParam.getItemId())
                    .remark(orderParam.getRemark())
                    .state(OrderStateEnum.WAIT_PAY.getOrderState())
                    .time(new Date())
                    .totalPrice(totalPrice)
                    .userId(userId)
                    .build();
            orderMapper.insertOrder(order);
        } else if (orderParams.size() > 1){
            List<Order> orders = new ArrayList<>();
            orderParams.forEach(orderParam -> {
                String orderId = UUIDUtils.getOrderId();
                orderIds.add(orderId);
                ItemCartVO itemCartVO = orderMapper.getItemCartVOByCartId(orderParam.getCartId());
                Integer itemId = itemCartVO.getItemId();
                String title = itemCartVO.getTitle();
                Integer price = itemCartVO.getPrice();
                Integer amount = itemCartVO.getAmount();
                Integer couponOwnId = orderParam.getCouponOwnId();
                int totalPrice;
                if (couponOwnId == -1) {
                    totalPrice = price * amount;
                } else {
                    Integer discount = orderMapper.getDiscountByCouponOwnId(couponOwnId);
                    totalPrice = price * amount - discount;
                }
                Order order = Order.builder()
                        .orderId(orderId)
                        .orderName(title)
                        .addressId(addressId)
                        .amount(amount)
                        .couponOwnId(couponOwnId)
                        .itemId(itemId)
                        .remark(orderParam.getRemark())
                        .state(OrderStateEnum.WAIT_PAY.getOrderState())
                        .time(new Date())
                        .totalPrice(totalPrice)
                        .userId(userId)
                        .build();
                orders.add(order);
            });


        } else {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数错误！");
        }

        return ApiResponse.ok(orderIds);
    }























}
