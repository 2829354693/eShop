package com.yc.eshop.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 余聪
 * @date 2020/12/1
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor())
                .addPathPatterns("/user/**")
                .addPathPatterns("/order/**")
                .addPathPatterns("/comment/**")
                .addPathPatterns("/merchant/**")
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/merchant/merchantLogin")
                .excludePathPatterns("/merchant/merchantLogout")
                .excludePathPatterns("/admin/adminLogin");
    }
}
