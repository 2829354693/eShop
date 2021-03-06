package com.yc.eshop.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yc.eshop.admin.mapper.AdminMapper;
import com.yc.eshop.admin.service.AdminService;
import com.yc.eshop.common.dto.PageParam;
import com.yc.eshop.common.entity.AdminUser;
import com.yc.eshop.common.entity.MerchantUser;
import com.yc.eshop.common.entity.ShopApply;
import com.yc.eshop.common.entity.Store;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.response.ResponseCode;
import com.yc.eshop.common.service.RedisService;
import com.yc.eshop.common.util.ShiroUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class AdminServiceImpl implements AdminService {

    @Value("${picture-nginx-host}")
    private String pictureNginxHost;

    @Value("${admin-account}")
    private String adminAccount;

    @Value("${admin-password}")
    private String adminPassword;

    @Value("${admin-token-expire}")
    private Integer adminTokenExpire;

    @Autowired
    RedisService redisService;

    @Autowired
    AdminMapper adminMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ApiResponse<?> adminLogin(AdminUser adminUser) {
        String account = adminUser.getAccount();
        String password = adminUser.getPassword();

        if (Objects.equals(account, adminAccount) && Objects.equals(password, adminPassword)) {
            String token = UUID.randomUUID().toString();
            redisService.setExpire(token, account, adminTokenExpire);
            adminUser.setPassword(null);
            adminUser.setToken(token);
            return ApiResponse.ok(adminUser);
        } else {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "账号或密码错误！");
        }

    }

    @Override
    public ApiResponse<?> adminLogout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Boolean delete = redisService.delete(token);
        if (!delete) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "已退出登录！");
        }
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> getShopApplyList(PageParam pageParam) {
        PageHelper.startPage(pageParam.getCurrentPage(), pageParam.getPageSize());
        List<ShopApply> shopApplyList = adminMapper.getShopApplyList();
        for (ShopApply shopApply : shopApplyList) {
            shopApply.setApplyShopLogo(pictureNginxHost + shopApply.getApplyShopLogo());
            shopApply.setApplyTimeStr(sdf.format(shopApply.getApplyTime()));
            if (Objects.nonNull(shopApply.getHandleTime())) {
                shopApply.setHandleTimeStr(sdf.format(shopApply.getHandleTime()));
            }
        }
        PageInfo<ShopApply> pageInfo = new PageInfo<>(shopApplyList);
        return ApiResponse.ok(pageInfo);
    }

    @Override
    public ApiResponse<?> consentShopApply(Integer shopApplyId) {
        adminMapper.updateShopApplyState(2, shopApplyId);
        String salt = ShiroUtil.createSalt();
        String pwd = ShiroUtil.saltEncrypt("111111", salt);
        ShopApply apply = adminMapper.getShopApplyById(shopApplyId);
        MerchantUser merchantUser = MerchantUser.builder()
                .account(apply.getApplyAccount())
                .password(pwd)
                .nickname(apply.getApplyAccount())
                .salt(salt)
                .build();
        adminMapper.insertMerchantUser(merchantUser);
        Store store = Store.builder()
                .logo(apply.getApplyShopLogo())
                .merchantId(merchantUser.getMerchantId())
                .name(apply.getApplyShopName())
                .build();
        adminMapper.insertStore(store);
        return ApiResponse.ok();
    }

    @Override
    public ApiResponse<?> refuseShopApply(Integer shopApplyId) {
        adminMapper.updateShopApplyState(3, shopApplyId);
        return ApiResponse.ok();
    }


}
