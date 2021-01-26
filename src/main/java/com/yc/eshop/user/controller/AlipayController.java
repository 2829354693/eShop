package com.yc.eshop.user.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.user.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class AlipayController {

    @Autowired
    OrderService orderService;

    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjw6LI9mDvsD63+JcumJWjk388+2IO3JKDUgzW3SUbHDzrhlPGElzPah4tn3iEmzKuePRJrzNNvuvbvbG57b0fusrE25yMovZAQNKqyTMz4RDGAl/O+qyKZku/kFwtO9Id8eOP+shU8N72ik7K0vzIBhp9iK19NHrX1+6LvfOyM1AUA1f94o/AOUwRj5Jd7pjJ0ECAGXBr80HYJmMNuE9XS7d78LKXMmJVeQ+Mpb/fqwt4UEEbZopuI/2dF6JROeXoIoL9wQO13QPM2Oo+RsjLGnD8t35ifnklzfL2kSB5jKYt6T0mKDIpI6SqPrstd0EIP5wHI5ZJapXdveg23lRgwIDAQAB";
    private final String CHARSET = "UTF-8";
    private final String SIGN_TYPE = "RSA2";

    @RequestMapping("returnUrl")
    public ModelAndView returnUrl(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, AlipayApiException {
        // 获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE); // 调用SDK验证签名
        //验证签名通过
        if(signVerified){
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            // 付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            //支付成功，修复支付状态
            String[] orderIds = out_trade_no.split(",");
            List<String> orderIdList = Arrays.asList(orderIds);
            orderService.updateOrderFromAliPay(orderIdList);

            return new ModelAndView("alipay_ok");
        }else{
            return new ModelAndView("alipay_fail");
        }
    }
}
