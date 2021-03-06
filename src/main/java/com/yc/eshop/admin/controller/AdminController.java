package com.yc.eshop.admin.controller;

import com.yc.eshop.admin.service.AdminService;
import com.yc.eshop.common.dto.PageParam;
import com.yc.eshop.common.entity.AdminUser;
import com.yc.eshop.common.response.ApiResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Validated
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping("/adminLogin")
    public ApiResponse<?> adminLogin(@RequestBody AdminUser adminUser) {
        return adminService.adminLogin(adminUser);
    }

    @GetMapping("/adminLogout")
    public ApiResponse<?> adminLogout(HttpServletRequest request) {
        return adminService.adminLogout(request);
    }

    @PostMapping("/getShopApplyList")
    public ApiResponse<?> getShopApplyList(@RequestBody PageParam pageParam) {
        return adminService.getShopApplyList(pageParam);
    }

    @GetMapping("/consentShopApply/{shopApplyId}")
    public ApiResponse<?> consentShopApply(@PathVariable Integer shopApplyId) {
        return adminService.consentShopApply(shopApplyId);
    }

    @GetMapping("/refuseShopApply/{shopApplyId}")
    public ApiResponse<?> refuseShopApply(@PathVariable Integer shopApplyId) {
        return adminService.refuseShopApply(shopApplyId);
    }















}
