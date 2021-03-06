package com.yc.eshop.user.mapper;

import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.Order;
import com.yc.eshop.common.entity.RefundApply;
import com.yc.eshop.common.vo.ItemCartVO;
import com.yc.eshop.common.vo.OrderDetailVO;
import com.yc.eshop.common.vo.OrderInfoVO;
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

    List<OrderInfoVO> getWaitPayOrderInfo(Integer userId);

    OrderDetailVO getWaitPayOrderDetailInfo(String orderId);

    Integer getCouponOwnIdByOid(String orderId);

    void updateCouponCanUse(Integer couponOwnId);

    void deleteOrder(String orderId);

    List<OrderInfoVO> getWaitDeliverOrderInfo(Integer userId);

    OrderDetailVO getWaitDeliverOrderDetailInfo(String orderId);

    Integer getUidByOid(String orderId);

    Integer getPriceByOid(String orderId);

    void refundByUid(Integer userId, Integer refundMoney);

    List<OrderInfoVO> getWaitReceiveOrderInfo(Integer userId);

    List<OrderInfoVO> getOrderInfos(Integer userId, Integer orderState);

    OrderDetailVO getWaitReceiveOrderDetailInfo(String orderId);

    void updateOrderState(String orderId, Integer state);

    void updateOrderFinishTime(String orderId);

    List<OrderInfoVO> getFinishOrderInfo(Integer userId);

    OrderDetailVO getFinishOrderDetailInfo(String orderId);

    void updateOrderRefundCheck(String orderId);

    void insertRefundApply(RefundApply refundApply);

    List<OrderInfoVO> getRefundOrderInfo(Integer userId);

    OrderDetailVO getRefundOrderDetailInfo(String orderId);

    List<OrderInfoVO> getAllOrderInfo(Integer userId);














}
