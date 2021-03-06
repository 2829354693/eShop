package com.yc.eshop.visitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.eshop.common.dto.SearchItemParam;
import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.ShopApply;
import com.yc.eshop.common.entity.Store;
import com.yc.eshop.common.entity.StoreCoupon;
import com.yc.eshop.common.vo.ItemCommentVO;
import com.yc.eshop.common.vo.ItemStoreVO;
import com.yc.eshop.common.vo.StoreThreeItemsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @author 余聪
 * @date 2020/12/7
 */
@Mapper
public interface IndexMapper extends BaseMapper<Item> {

    List<Item> getIndexHotItems();

    ItemStoreVO getItemByItemId(Integer itemId);

    Integer getStoreIdByItemId(Integer itemId);

    List<Item> getTopTwoItemByStoreId(Integer storeId);

    List<Item> getIndexGoodItems();

    List<Item> getRecommendItems();

    List<Store> getRankingStore();

    List<ItemStoreVO> getSearchItems(SearchItemParam searchItemParamDTO);

    List<StoreThreeItemsVO> getSearchStoreByName(String name);

    List<StoreThreeItemsVO> getSearchStoreByType(String type);

    List<Item> getHotThreeItems(Integer storeId);

    Store getStoreByStoreId(Integer storeId);

    List<Item> getStoreItemsByStoreId(Integer storeId);

    List<StoreCoupon> getStoreCoupon(Integer storeId);

    List<ItemCommentVO> getCommentByIid(Integer itemId);

    Integer getShopApplyCountByName(String name);

    Integer getShopApplyCountByAccount(String account);

    void insertShopApply(String account, String name, String type, String logo, Date time);










}
