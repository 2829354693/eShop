package com.yc.eshop.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yc.eshop.common.dto.SearchItemParam;
import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.Store;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.vo.ItemStoreVO;
import com.yc.eshop.common.vo.StoreThreeItemsVO;

import java.util.List;

/**
 * @author 余聪
 * @date 2020/12/7
 */
public interface IndexService extends IService<Item> {

    ApiResponse<List<Item>> indexHotItems() throws Exception;

    ApiResponse<ItemStoreVO> getItemByItemId(Integer itemId) throws Exception;

    ApiResponse<List<Item>> hotStoreItems(Integer itemId);

    ApiResponse<List<Item>> indexGoodItems();

    ApiResponse<List<Item>> recommendItems();

    ApiResponse<List<Store>> rankingStore();

    ApiResponse<List<ItemStoreVO>> searchItems(SearchItemParam searchItemParamDTO);

    ApiResponse<List<StoreThreeItemsVO>> searchStores(SearchItemParam searchItemParamDTO) throws Exception;

    ApiResponse<Store> getStoreData(Integer storeId) throws Exception;

    ApiResponse<List<Item>> getStoreItems(Integer storeId) throws Exception;















}
