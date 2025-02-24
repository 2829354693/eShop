package com.yc.eshop.visitor.controller;

import com.yc.eshop.common.dto.SearchItemParam;
import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.ShopApply;
import com.yc.eshop.common.entity.Store;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.vo.ItemStoreVO;
import com.yc.eshop.common.vo.StoreThreeItemsVO;
import com.yc.eshop.visitor.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author 余聪
 * @date 2020/12/7
 */
@Validated
@Api(value = "/index", tags = "游客Controller")
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    IndexService indexService;

    @ApiOperation("获取主页的两个热卖商品")
    @GetMapping("/indexHotItems")
    public ApiResponse<List<Item>> indexHotItems() throws Exception {
        return indexService.indexHotItems();
    }

    @ApiOperation("商品详情页商品数据")
    @GetMapping("/getItemByItemId")
    public ApiResponse<ItemStoreVO> getItemByItemId(Integer itemId) throws Exception {
        return indexService.getItemByItemId(itemId);
    }

    @ApiOperation("商品详情页的两个店铺热卖商品")
    @GetMapping("/hotStoreItems")
    public ApiResponse<List<Item>> hotStoreItems(Integer itemId) {
        return indexService.hotStoreItems(itemId);
    }

    @ApiOperation("主页随机4个商品")
    @GetMapping("/indexGoodItems")
    public ApiResponse<List<Item>> indexGoodItems() {
        return indexService.indexGoodItems();
    }

    @ApiOperation("主页8个推荐商品")
    @GetMapping("/recommendItems")
    public ApiResponse<List<Item>> getRecommendItems() {
        return indexService.recommendItems();
    }

    @ApiOperation("店铺排行榜")
    @GetMapping("/rankingStore")
    public ApiResponse<List<Store>> rankingStore() {
        return indexService.rankingStore();
    }

    @ApiOperation("主页搜索商品")
    @PostMapping("/searchItems")
    public ApiResponse<List<ItemStoreVO>> searchItems(@RequestBody SearchItemParam searchItemParamDTO) {
        return indexService.searchItems(searchItemParamDTO);
    }

    @ApiOperation("搜索店铺")
    @PostMapping("/searchStores")
    public ApiResponse<List<StoreThreeItemsVO>> searchStores(@RequestBody SearchItemParam searchItemParamDTO) throws Exception {
        return indexService.searchStores(searchItemParamDTO);
    }

    @ApiOperation("获取店铺数据")
    @GetMapping("/getStoreData")
    public ApiResponse<Store> getStoreData(Integer storeId) throws Exception {
        return indexService.getStoreData(storeId);
    }

    @ApiOperation("获取店铺所有商品")
    @GetMapping("/getStoreItems")
    public ApiResponse<List<Item>> getStoreItems(Integer storeId) throws Exception {
        return indexService.getStoreItems(storeId);
    }

    @ApiOperation("/获取店铺主页优惠券")
    @GetMapping("/getStoreCoupon")
    public ApiResponse<?> getStoreCoupon(Integer storeId) {
        return indexService.getStoreCoupon(storeId);
    }

    @ApiOperation("/获取商品页的评论")
    @GetMapping("/getCommentByIid/{itemId}")
    public ApiResponse<?> getCommentByIid(@PathVariable Integer itemId) {
        return indexService.getCommentByIid(itemId);
    }

    @ApiOperation("店铺入驻申请提交")
    @PostMapping("/shopApplyCommit")
    public ApiResponse<?> shopApplyCommit(MultipartFile logo, String account,String type,String name) throws IOException {
        ShopApply shopApply = ShopApply.builder()
                .logoFile(logo)
                .applyAccount(account)
                .applyShopType(type)
                .applyShopName(name)
                .build();
        return indexService.shopApplyCommit(shopApply);
    }












}
