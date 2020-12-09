package com.yc.eshop.visitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.eshop.common.dto.SearchItemParam;
import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.Store;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.vo.ItemStoreVO;
import com.yc.eshop.visitor.mapper.IndexMapper;
import com.yc.eshop.visitor.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author 余聪
 * @date 2020/12/7
 */
@Service
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
            itemAndStore.setDesPic(pictureNginxHost + itemAndStore.getDesPic());
            itemAndStore.setLogo(pictureNginxHost + itemAndStore.getLogo());
            return ApiResponse.ok(itemAndStore);
        } else {
            throw new Exception("未找到该商品！");
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
        List<ItemStoreVO> items = indexMapper.searchItems(searchItemParamDTO);
        items.forEach(item -> {
            item.setPicture(pictureNginxHost + item.getPicture());
            item.setLogo(pictureNginxHost + item.getLogo());
        });
        return ApiResponse.ok(items);
    }

    private List<Item> changePicPath(List<Item> obj) {
        obj.forEach(item -> {
            item.setPicture(pictureNginxHost + item.getPicture());
        });
        return obj;
    }
}
