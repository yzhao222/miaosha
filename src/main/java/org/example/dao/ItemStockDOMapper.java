package org.example.dao;

import org.apache.ibatis.annotations.Param;
import org.example.dataobject.ItemStockDO;

public interface ItemStockDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemStockDO record);

    int insertSelective(ItemStockDO record);

    ItemStockDO selectByPrimaryKey(Integer id);
    ItemStockDO selectByItemId(Integer itemId);
    int decreaseStock(@Param("itemId")Integer itemId,@Param("amount") Integer amount);

    int updateByPrimaryKeySelective(ItemStockDO record);

    int updateByPrimaryKey(ItemStockDO record);
}