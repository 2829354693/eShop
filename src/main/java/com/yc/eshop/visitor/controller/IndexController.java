package com.yc.eshop.visitor.controller;

import com.yc.eshop.common.dto.SearchItemParam;
import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.Store;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.vo.ItemStoreVO;
import com.yc.eshop.visitor.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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


}
