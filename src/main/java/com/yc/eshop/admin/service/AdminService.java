package com.yc.eshop.admin.service;

import com.yc.eshop.common.dto.PageParam;
import com.yc.eshop.common.entity.AdminUser;
import com.yc.eshop.common.response.ApiResponse;

import javax.servlet.http.HttpServletRequest;

public interface AdminService {
    ApiResponse<?> adminLogin(AdminUser adminUser);

    ApiResponse<?> adminLogout(HttpServletRequest request);

    ApiResponse<?> getShopApplyList(PageParam pageParam);

    ApiResponse<?> consentShopApply(Integer shopApplyId);

    ApiResponse<?> refuseShopApply(Integer shopApplyId);
}
