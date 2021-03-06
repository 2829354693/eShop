package com.yc.eshop.visitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.eshop.common.dto.SearchItemParam;
import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.ShopApply;
import com.yc.eshop.common.entity.Store;
import com.yc.eshop.common.entity.StoreCoupon;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.response.ResponseCode;
import com.yc.eshop.common.vo.ItemCommentVO;
import com.yc.eshop.common.vo.ItemStoreVO;
import com.yc.eshop.common.vo.StoreThreeItemsVO;
import com.yc.eshop.visitor.mapper.IndexMapper;
import com.yc.eshop.visitor.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @Value("${store-logo-save-path}")
    private String storeLogoSavePath;

    @Autowired
    IndexMapper indexMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

    @Override
    public ApiResponse<?> getCommentByIid(Integer itemId) {
        if (Objects.isNull(itemId)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "参数为空！");
        }
        List<ItemCommentVO> comments = indexMapper.getCommentByIid(itemId);
        if (Objects.isNull(comments)) {
            return ApiResponse.ok(Collections.emptyList());
        }
        for (ItemCommentVO commentVO : comments) {
            if (!"".equals(commentVO.getHeadPic())) {
                commentVO.setHeadPic(pictureNginxHost + commentVO.getHeadPic());
            }
            String[] pics = commentVO.getPicture().split(",");
            for (int i = 0; i < pics.length; i++) {
                pics[i] = pictureNginxHost + pics[i];
            }
            commentVO.setPictures(pics);
            commentVO.setTimeStr(sdf.format(commentVO.getTime()));
        }
        return ApiResponse.ok(comments);
    }

    @Override
    public ApiResponse<?> shopApplyCommit(ShopApply shopApply) throws IOException {
        Integer count = indexMapper.getShopApplyCountByName(shopApply.getApplyShopName());
        if (count == 1) {
            return ApiResponse.ok("该店铺名称已存在！");
        }
        Integer count1 = indexMapper.getShopApplyCountByAccount(shopApply.getApplyAccount());
        if (count1 == 1) {
            return ApiResponse.ok("该账号已被占用！");
        }
        String originalFilename = shopApply.getLogoFile().getOriginalFilename();
        if (Objects.isNull(originalFilename)) {
            return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "上传头像失败！图片名为空！");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newPicName = df.format(new Date()) + "_" + shopApply.getApplyAccount().substring(5) + originalFilename.substring(originalFilename.lastIndexOf("."));
        File newPic = new File(storeLogoSavePath + newPicName);
        shopApply.getLogoFile().transferTo(newPic);
        indexMapper.insertShopApply(shopApply.getApplyAccount(),shopApply.getApplyShopName(),shopApply.getApplyShopType(),"store_pic/"+newPicName,new Date());
        return ApiResponse.ok();
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
