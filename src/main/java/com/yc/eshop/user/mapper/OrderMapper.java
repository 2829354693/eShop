package com.yc.eshop.user.mapper;

import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.Order;
import com.yc.eshop.common.vo.ItemCartVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 余聪
 * @date 2021/1/19
 */
@Mapper
public interface OrderMapper {

    Item getItemByIid(Integer itemId);

    void insertOrder(Order order);

    Integer getDiscountByCouponOwnId(Integer couponOwnId);

    ItemCartVO getItemCartVOByCartId(Integer cartId);

    void insertManyOrders(@Param("orders") List<Order> orders);

    List<Integer> getTotalPrice(List<String> orderIds);

    List<Order> getOrdersByOid(List<String> orderIds);

    void updateItemSalesAndInventory(Integer itemId, Integer salesNum);

    void updateOrderPay(String orderId, Integer payWay);

    void deleteCart(List<Integer> cartIds);

    void updateCouponUserState(List<Integer> couponOwnIds);








}
