package com.yc.eshop.interceptor;

import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.response.ResponseCode;
import com.yc.eshop.common.service.RedisService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author 余聪
 * @date 2020/12/1
 */
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            response.getWriter().write(JSONObject.fromObject(ApiResponse.failure(ResponseCode.UNAUTHORIZED, "未登录或登录已失效！")).toString());
            return false;
        }
        Object loginUserInfo = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext()).getBean(RedisService.class).get(token);
        if (Objects.isNull(loginUserInfo)) {
            response.getWriter().write(JSONObject.fromObject(ApiResponse.failure(ResponseCode.UNAUTHORIZED, "token错误！")).toString());
            return false;
        }
        return true;
    }
}
