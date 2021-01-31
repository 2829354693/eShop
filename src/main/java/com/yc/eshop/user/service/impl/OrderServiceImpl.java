package com.yc.eshop.user.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.yc.eshop.common.dto.CreateOrderParam;
import com.yc.eshop.common.dto.OrderParam;
import com.yc.eshop.common.dto.PayOrderParam;
import com.yc.eshop.common.dto.RefundApplyParam;
import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.Order;
import com.yc.eshop.common.entity.RefundApply;
import com.yc.eshop.common.enums.OrderStateEnum;
import com.yc.eshop.common.enums.PayWayEnum;
import com.yc.eshop.common.enums.RefundEnum;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.response.ResponseCode;
import com.yc.eshop.common.util.UUIDUtils;
import com.yc.eshop.common.vo.ItemCartVO;
import com.yc.eshop.common.vo.OrderDetailVO;
import com.yc.eshop.common.vo.OrderInfoVO;
import com.yc.eshop.user.mapper.OrderMapper;
import com.yc.eshop.user.mapper.UserMapper;
import com.yc.eshop.user.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 余聪
 * @date 2021/1/19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Value("${picture-nginx-host}")
    private String pictureNginxHost;

    @Autowired
    UserMapper userMapper;

    private final String APP_ID = "2016110300789091";
    private final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQClDR0RIMEMThu5odxo8hNJvxJmP1Z3PoLKN1DEUQsmQRd4RrzPQ3c4b5vnBfnzgtAKL4PChnmZcMTni7oKBPBgtTMwJQsXMAGDBtfzOMAsFwvOWluqSDXzJI8Sxcj4dW7T/iPetXGBPTmBn04WR+y957sh/tTDHVSXe5zxX8tjkFDmT7J9bCj95GQAftSKqr2c5sncjGH61H/INBwqWZXgSsQnV85hHHKnJCjHv4vIUUU52wM0ynLVGH5uInkEzd+MkpMCGEH4bENCQ0DTxVT0IeN3IhUw+R6obIJwuLIj8G9B0fgmcPI7kvWQGQk5Tvj1WogjI8yPZ6uDUookK1prAgMBAAECggEBAJ0rx6ovzqWxG6XCBOnK26M2WIcVeXlEBbgaxMathjh/c6INem7EiBst7QAqKMNCpmmMpaPv+ourX1Y/MIGqAl2VL5GBNPZ3uHjVY38xY46Yw51VXkThTr6i6buurJos+cTWFcJWdifj7YcJ9IozI7YTnZfZ2zGSFKPMCr+vwTalBcMeteI1wfRQaGGXcfsS3LAjkwAGnf/aClklfsgYFeaF4ptEyRmvm3jSdWKoUiVeUIkcnMRssk46T9Wk/RGn2IyKL4eB6pzdivh+5kwBnPhZCxTJwf42QzESCTUk32qJfU+EknYDPNVr/XqIfOixX/UAMehV+UGmyfDRL+SKQEECgYEA4URM/fkBTbAf6iAecw042M5I1trA8ZA3F9LlAgyboYMhTFFL0asGT7u0n4u0wtCmfAD5Iq1X5fin320b4QRMjgKV0wdBSe6wTjZqv6RAjtWyAkdY5obuV9OncXZUA6gdLWYgpMZcHLk/41FMTNbh7EVztMnlY5LGu3QXdcttd/cCgYEAu5G3IAUK2hTBgNuvOUebNeJF237qp9ERNK+j/4MVWkOl74uW0KUu5D0fu/KTaZR2KEK0w2qxwniPm474JLlOcba8zpvlbqQiYKizMou7mx8Z/Dx3RxzMtx1nVWdJIUcNiiX7J31Dt4MkOwJcRtTlcy3ZmucCvlf0zS1osPST3C0CgYA9Zyi1cdJxIrkQUID8S3ZbYp9oMWi2b+EUlV/nFxi1BdFlxCMum7e0tC2f76zuDYHT+0/tq+JxxT29ITigNVPIwdulEi0xweY864hdhKA0Tvues2QAMnd1aTQTMXXlKUYKwfPT9l3VVombpOVvbINYq/XGKeJlKfUR06sxaKoGMQKBgHnJxmbC/2XUnkQF4INHrE7Tadp4UPK/+/UCqnCIc3/VdtPQBg/ZgVXvHkDR48dk/0dq8KYGC/+ielReA18M9xM8vOFGNjWd2zh5sUj7k8fH7P5rYGNLHt0y+VppowE5emAeAzKD0klAnHC4OqQ/LfCYb7d1kcF3qNrkRhrH443NAoGAM5oiJzTZH2ljwM9G8u/fX6BAkhmNX7lTuswN0saQj+xk4kCv6i0dVg9MVUB4X7J1QghukW0J9ZyCoCS/UpBxXr5e/CTCZqGxmmYouTGn3VmHHhROj7neB/5w8VwDrCiWkDlePXxFNylr3ESbn/G5IirAKiywTmmgUhBO1NCdvdw=";
    private final String CHARSET = "UTF-8";
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjw6LI9mDvsD63+JcumJWjk388+2IO3JKDUgzW3SUbHDzrhlPGElzPah4tn3iEmzKuePRJrzNNvuvbvbG57b0fusrE25yMovZAQNKqyTMz4RDGAl/O+qyKZku/kFwtO9Id8eOP+shU8N72ik7K0vzIBhp9iK19NHrX1+6LvfOyM1AUA1f94o/AOUwRj5Jd7pjJ0ECAGXBr80HYJmMNuE9XS7d78LKXMmJVeQ+Mpb/fqwt4UEEbZopuI/2dF6JROeXoIoL9wQO13QPM2Oo+RsjLGnD8t35ifnklzfL2kSB5jKYt6T0mKDIpI6SqPrstd0EIP5wHI5ZJapXdveg23lRgwIDAQAB";
    //这是沙箱接口路径,正式路径为https://openapi.alipay.com/gateway.do
    private final String GATEWAY_URL ="https://openapi.alipaydev.com/gateway.do";
    private final String FORMAT = "JSON";
    //签名方式
    private final String SIGN_TYPE = "RSA2";
    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    private final String NOTIFY_URL = "http://127.0.0.1/notifyUrl";
    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    @Value("${return-url}")
    private String RETURN_URL;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ApiResponse<?> createOrder(CreateOrderParam createOrderParam) {
        Integer userId = createOrderParam.getUserId();
        Integer addressId = createOrderParam.getAddressId();
        List<OrderParam> orderParams = createOrderParam.getOrders();
        List<String> orderIds = new ArrayList<>();
        if (Objects.isNull(orderParams.get(0).getCartId())) {
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
        } else {
            List<Order> orders = new ArrayList<>();
            List<Integer> cartIds = new ArrayList<>();
            orderParams.forEach(orderParam -> {
                cartIds.add(orderParam.getCartId());
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

            List<Integer> couponOwnIds = new ArrayList<>();
            orderParams.forEach(orderParam -> {
                if (orderParam.getCouponOwnId() != -1) {
                    couponOwnIds.add(orderParam.getCouponOwnId());
                }
            });
            if (couponOwnIds.size() != 0) {
                orderMapper.updateCouponUserState(couponOwnIds);
            }
            orderMapper.insertManyOrders(orders);
            orderMapper.deleteCart(cartIds);
        }

        return ApiResponse.ok(orderIds);
    }

    @Override
    public ApiResponse<?> getOrderTotalPrice(PayOrderParam payOrderParam) {
        if (Objects.isNull(payOrderParam) || Objects.isNull(payOrderParam.getOrderIds())) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        List<String> orderIds = payOrderParam.getOrderIds();
        List<Integer> prices = orderMapper.getTotalPrice(orderIds);
        Integer totalPrice = 0;
        for (Integer p:prices) {
            totalPrice += p;
        }
        return ApiResponse.ok(totalPrice);
    }

    @Override
    public ApiResponse<?> payNow(PayOrderParam payOrderParam) {
        if (Objects.isNull(payOrderParam)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        List<String> orderIds = payOrderParam.getOrderIds();
        Integer userId = payOrderParam.getUserId();
        Integer totalPrice = payOrderParam.getTotalPrice();
        if (Objects.isNull(orderIds) || Objects.isNull(userId) || Objects.isNull(totalPrice)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }

        Integer coin = userMapper.getCoinByUid(userId);
        userMapper.updateCoinById(userId, coin - totalPrice);

        List<Order> orders = orderMapper.getOrdersByOid(orderIds);
        for (Order order : orders) {
            orderMapper.updateItemSalesAndInventory(order.getItemId(), order.getAmount());
            orderMapper.updateOrderPay(order.getOrderId(), PayWayEnum.COIN_PAY.getPayWay());
        }

        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> getAliPayForm(PayOrderParam payOrderParam) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(RETURN_URL);
        request.setNotifyUrl(NOTIFY_URL);
        List<String> orderIds = payOrderParam.getOrderIds();
        List<Order> orders = orderMapper.getOrdersByOid(orderIds);
        StringBuilder orderIdStr = new StringBuilder();
        StringBuilder orderNameStr = new StringBuilder();
        Integer totalPrice = 0;
        for (Order order:orders) {
            orderIdStr.append(order.getOrderId()).append(",");
            orderNameStr.append(order.getOrderName()).append(",");
            totalPrice += order.getTotalPrice();
        }
        //商户订单号，商户网站订单系统中唯一订单号，必填
        //生成随机Id
        String out_trade_no = orderIdStr.deleteCharAt(orderIdStr.length()-1).toString();
        //付款金额，必填
        String total_amount = BigDecimal.valueOf(Long.valueOf(totalPrice)).divide(new BigDecimal(100)).toString();
        //订单名称，必填
        String subject = orderNameStr.substring(0,20);
        //商品描述，可空
        String body = "";

        request.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\"" + total_amount +"\","
                + "\"subject\":\"" + subject +"\","
                + "\"body\":\"" + body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String form = "";
        form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单

        return ApiResponse.ok(form);
    }

    @Override
    public void updateOrderFromAliPay(List<String> orderIds) {
        List<Order> orders = orderMapper.getOrdersByOid(orderIds);
        for (Order order : orders) {
            orderMapper.updateItemSalesAndInventory(order.getItemId(), order.getAmount());
            orderMapper.updateOrderPay(order.getOrderId(), PayWayEnum.ALIPAY.getPayWay());
        }
    }

    @Override
    public ApiResponse<?> getWaitPayOrderInfo(Integer userId) {
        if (Objects.isNull(userId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        List<OrderInfoVO> orderInfos = orderMapper.getOrderInfos(userId, OrderStateEnum.WAIT_PAY.getOrderState());
        if (Objects.isNull(orderInfos)) {
            return ApiResponse.ok();
        }

        for (OrderInfoVO order:orderInfos) {
            order.setPicture(pictureNginxHost + order.getPicture());
            order.setLogo(pictureNginxHost + order.getLogo());
            if (Objects.isNull(order.getDiscount())) {
                order.setDiscount(0);
            }
        }

        return ApiResponse.ok(orderInfos);
    }

    @Override
    public ApiResponse<?> getWaitPayOrderDetailInfo(String orderId) {
        if (Objects.isNull(orderId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }

        OrderDetailVO order = orderMapper.getWaitPayOrderDetailInfo(orderId);
        handlePicPathAndDate(order);
        return ApiResponse.ok(order);
    }

    private void handlePicPathAndDate(OrderDetailVO order) {
        order.setPicture(pictureNginxHost + order.getPicture());
        order.setLogo(pictureNginxHost + order.getLogo());
        order.setCreateTimeStr(sdf.format(order.getCreateTime()));
        if (Objects.isNull(order.getDiscount())) {
            order.setDiscount(0);
        }
    }

    private void handlePicPath(List<OrderInfoVO> orders) {
        orders.forEach(order -> {
            order.setPicture(pictureNginxHost + order.getPicture());
            order.setLogo(pictureNginxHost + order.getLogo());
            if (Objects.isNull(order.getDiscount())) {
                order.setDiscount(0);
            }
        });
    }

    @Override
    public ApiResponse<?> delOrder(String orderId) {
        if (Objects.isNull(orderId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }

        Integer couponOwnId = orderMapper.getCouponOwnIdByOid(orderId);

        if (couponOwnId != -1) {
            orderMapper.updateCouponCanUse(couponOwnId);
        }

        orderMapper.deleteOrder(orderId);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> delWaitDeliverOrder(String orderId) {
        if (Objects.isNull(orderId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        Integer couponOwnId = orderMapper.getCouponOwnIdByOid(orderId);
        if (couponOwnId != -1) {
            orderMapper.updateCouponCanUse(couponOwnId);
        }
        Integer userId = orderMapper.getUidByOid(orderId);
        Integer price = orderMapper.getPriceByOid(orderId);
        orderMapper.refundByUid(userId, price);
        orderMapper.deleteOrder(orderId);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> getWaitDeliverOrderInfo(Integer userId) {
        if (Objects.isNull(userId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        List<OrderInfoVO> orders = orderMapper.getOrderInfos(userId, OrderStateEnum.WAIT_DELIVER.getOrderState());
        if (Objects.isNull(orders)) {
            return ApiResponse.ok();
        }
        handlePicPath(orders);
        return ApiResponse.ok(orders);
    }

    @Override
    public ApiResponse<?> getWaitDeliverOrderDetailInfo(String orderId) {
        if (Objects.isNull(orderId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }

        OrderDetailVO order = orderMapper.getWaitDeliverOrderDetailInfo(orderId);
        handlePicPathAndDate(order);
        order.setPayTimeStr(sdf.format(order.getPayTime()));

        return ApiResponse.ok(order);
    }

    @Override
    public ApiResponse<?> getWaitReceiveOrderInfo(Integer userId) {
        if (Objects.isNull(userId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        List<OrderInfoVO> orders = orderMapper.getOrderInfos(userId, OrderStateEnum.WAIT_RECEIVE.getOrderState());
        if (Objects.isNull(orders)) {
            return ApiResponse.ok();
        }
        handlePicPath(orders);
        return ApiResponse.ok(orders);
    }

    @Override
    public ApiResponse<?> getWaitReceiveOrderDetailInfo(String orderId) {
        if (Objects.isNull(orderId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }

        OrderDetailVO order = orderMapper.getWaitReceiveOrderDetailInfo(orderId);
        handlePicPathAndDate(order);
        order.setPayTimeStr(sdf.format(order.getPayTime()));
        order.setDeliverTimeStr(sdf.format(order.getDeliverTime()));

        return ApiResponse.ok(order);
    }

    @Override
    public ApiResponse<?> confirmReceive(String orderId) {
        if (Objects.isNull(orderId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        orderMapper.updateOrderState(orderId, OrderStateEnum.FINISH.getOrderState());
        orderMapper.updateOrderFinishTime(orderId);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> getFinishOrderInfo(Integer userId) {
        if (Objects.isNull(userId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        List<OrderInfoVO> orders = orderMapper.getFinishOrderInfo(userId);
        if (Objects.isNull(orders)) {
            return ApiResponse.ok();
        }
        handlePicPath(orders);
        return ApiResponse.ok(orders);
    }

    @Override
    public ApiResponse<?> getFinishOrderDetailInfo(String orderId) {
        if (Objects.isNull(orderId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        OrderDetailVO order = orderMapper.getFinishOrderDetailInfo(orderId);
        handlePicPathAndDate(order);
        order.setPayTimeStr(sdf.format(order.getPayTime()));
        order.setDeliverTimeStr(sdf.format(order.getDeliverTime()));
        order.setFinishTimeStr(sdf.format(order.getFinishTime()));

        return ApiResponse.ok(order);
    }

    @Override
    public ApiResponse<?> refundApply(RefundApplyParam refundApplyParam) {
        String orderId = refundApplyParam.getOrderId();
        String reason = refundApplyParam.getReason();

        orderMapper.updateOrderRefundCheck(orderId);

        RefundApply refundApply = RefundApply.builder()
                .orderId(orderId)
                .reason(reason)
                .status(RefundEnum.WAIT_CHECK.getRefundStatus())
                .build();
        orderMapper.insertRefundApply(refundApply);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> getRefundOrderInfo(Integer userId) {
        if (Objects.isNull(userId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        List<OrderInfoVO> orders = orderMapper.getRefundOrderInfo(userId);
        if (Objects.isNull(orders)) {
            return ApiResponse.ok();
        }
        handlePicPath(orders);
        return ApiResponse.ok(orders);
    }

    @Override
    public ApiResponse<?> getRefundOrderDetailInfo(String orderId) {
        if (Objects.isNull(orderId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        OrderDetailVO order = orderMapper.getRefundOrderDetailInfo(orderId);
        handlePicPathAndDate(order);
        order.setPayTimeStr(sdf.format(order.getPayTime()));
        order.setDeliverTimeStr(sdf.format(order.getDeliverTime()));
        order.setFinishTimeStr(sdf.format(order.getFinishTime()));
        order.setRefundTimeStr(sdf.format(order.getRefundTime()));

        return ApiResponse.ok(order);
    }

    @Override
    public ApiResponse<?> getAllOrderInfo(Integer userId) {
        if (Objects.isNull(userId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        List<OrderInfoVO> orders = orderMapper.getAllOrderInfo(userId);
        if (Objects.isNull(orders)) {
            return ApiResponse.ok();
        }
        handlePicPath(orders);
        return ApiResponse.ok(orders);
    }

    @Override
    public ApiResponse<?> getOrderDetailInfo(String orderId) {
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


}
