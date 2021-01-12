package com.yc.eshop.visitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.eshop.common.dto.SearchItemParam;
import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.Store;
import com.yc.eshop.common.entity.StoreCoupon;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.response.ResponseCode;
import com.yc.eshop.common.vo.ItemStoreVO;
import com.yc.eshop.common.vo.StoreThreeItemsVO;
import com.yc.eshop.visitor.mapper.IndexMapper;
import com.yc.eshop.visitor.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 余聪
 * @date 2020/12/7
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IndexServiceImpl extends ServiceImpl<IndexMapper, Item> implements IndexService {

    @Value("${picture-nginx-host}")
    private String pictureNginxHost;

    @Autowired
    IndexMapper indexMapper;

    @Override
    public ApiResponse<List<Item>> indexHotItems() throws Exception {
        List<Item> indexHotItems = indexMapper.getIndexHotItems();
        if (Objects.nonNull(indexHotItems)) {
            indexHotItems.forEach(item -> {
                if (Objects.nonNull(item.getPicture())) {
                    item.setPicture(pictureNginxHost + item.getPicture());
                }
            });
            return ApiResponse.ok(indexHotItems);
        } else {
            throw new Exception("获取数据失败！");
        }
    }

    @Override
    public ApiResponse<ItemStoreVO> getItemByItemId(Integer itemId) throws Exception {
        ItemStoreVO itemAndStore = indexMapper.getItemByItemId(itemId);
        if (Objects.nonNull(itemAndStore)) {
            itemAndStore.setPicture(pictureNginxHost + itemAndStore.getPicture());
            String picturesStr = itemAndStore.getPictures();
            if (picturesStr.contains(",")) {
                String[] pictureAry = picturesStr.split(",");
                List<String> pictureList = Arrays.asList(pictureAry);
                List<String> pics = pictureList.stream().map(p -> pictureNginxHost + p).collect(Collectors.toList());
                itemAndStore.setPictureList(pics);
            } else {
                itemAndStore.setPictureList(Collections.singletonList(pictureNginxHost + picturesStr));
            }
            itemAndStore.setDesPic(pictureNginxHost + itemAndStore.getDesPic());
            itemAndStore.setLogo(pictureNginxHost + itemAndStore.getLogo());
            return ApiResponse.ok(itemAndStore);
        } else {
            throw new Exception("未找到该商品或商店已关闭！");
        }
    }

    @Override
    public ApiResponse<List<Item>> hotStoreItems(Integer itemId) {
        Integer storeId = indexMapper.getStoreIdByItemId(itemId);
        List<Item> topTwoItem = indexMapper.getTopTwoItemByStoreId(storeId);
        return ApiResponse.ok(changePicPath(topTwoItem));
    }

    @Override
    public ApiResponse<List<Item>> indexGoodItems() {
        List<Item> indexGoodItems = indexMapper.getIndexGoodItems();
        return ApiResponse.ok(changePicPath(indexGoodItems));
    }

    @Override
    public ApiResponse<List<Item>> recommendItems() {
        List<Item> recommendItems = indexMapper.getRecommendItems();
        return ApiResponse.ok(changePicPath(recommendItems));
    }

    @Override
    public ApiResponse<List<Store>> rankingStore() {
        return ApiResponse.ok(indexMapper.getRankingStore());
    }

    @Override
    public ApiResponse<List<ItemStoreVO>> searchItems(SearchItemParam searchItemParamDTO) {
        List<ItemStoreVO> items = indexMapper.getSearchItems(searchItemParamDTO);
        items.forEach(item -> {
            item.setPicture(pictureNginxHost + item.getPicture());
            item.setLogo(pictureNginxHost + item.getLogo());
        });
        return ApiResponse.ok(items, (long) items.size());
    }

    @Override
    public ApiResponse<List<StoreThreeItemsVO>> searchStores(SearchItemParam searchItemParamDTO) throws Exception {
        if (Objects.nonNull(searchItemParamDTO)) {
            if (Objects.nonNull(searchItemParamDTO.getItemType())) {
                List<StoreThreeItemsVO> stores = indexMapper.getSearchStoreByType(searchItemParamDTO.getItemType());
                return ApiResponse.ok(assembleStores(stores), (long) stores.size());
            } else if (Objects.nonNull(searchItemParamDTO.getTitleKeywords())) {
                List<StoreThreeItemsVO> stores = indexMapper.getSearchStoreByName(searchItemParamDTO.getTitleKeywords());
                return ApiResponse.ok(assembleStores(stores), (long) stores.size());
            } else {
                throw new Exception("查询参数为空！");
            }
        } else {
            throw new Exception("查询参数为空！");
        }
    }

    @Override
    public ApiResponse<Store> getStoreData(Integer storeId) throws Exception {
        if (Objects.nonNull(storeId)) {
            Store store = indexMapper.getStoreByStoreId(storeId);
            store.setLogo(pictureNginxHost + store.getLogo());
            return ApiResponse.ok(store);
        } else {
            throw new Exception("店铺id为空！");
        }
    }

    @Override
    public ApiResponse<List<Item>> getStoreItems(Integer storeId) throws Exception {
        if (Objects.nonNull(storeId)) {
            List<Item> items = indexMapper.getStoreItemsByStoreId(storeId);
            return ApiResponse.ok(changePicPath(items), (long) items.size());
        } else {
            throw new Exception("店铺id为空！");
        }
    }

    @Override
    public ApiResponse<?> getStoreCoupon(Integer storeId) {
        if (Objects.isNull(storeId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        List<StoreCoupon> storeCouponList = indexMapper.getStoreCoupon(storeId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        storeCouponList.forEach(storeCoupon -> storeCoupon.setEndTimeStr(sdf.format(storeCoupon.getEndTime())));
        return ApiResponse.ok(storeCouponList, (long) storeCouponList.size());
    }

    private List<StoreThreeItemsVO> assembleStores(List<StoreThreeItemsVO> stores) {
        stores.forEach(store -> {
            store.setLogo(pictureNginxHost + store.getLogo());
            List<Item> hotThreeItems = indexMapper.getHotThreeItems(store.getStoreId());
            store.setThreeHotItems(changePicPath(hotThreeItems));
        });
        return stores;
    }

    private List<Item> changePicPath(List<Item> obj) {
        obj.forEach(item -> {
            item.setPicture(pictureNginxHost + item.getPicture());
        });
        return obj;
    }















}
