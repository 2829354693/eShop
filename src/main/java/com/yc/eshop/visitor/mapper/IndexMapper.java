package com.yc.eshop.visitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yc.eshop.common.dto.SearchItemParam;
import com.yc.eshop.common.entity.Item;
import com.yc.eshop.common.entity.Store;
import com.yc.eshop.common.vo.ItemStoreVO;
import org.apache.ibatis.annotations.Mapper;

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

    List<ItemStoreVO> searchItems(SearchItemParam searchItemParamDTO);
}
