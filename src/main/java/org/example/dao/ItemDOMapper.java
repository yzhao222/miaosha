package org.example.dao;

import org.apache.ibatis.annotations.Param;
import org.example.dataobject.ItemDO;

import java.util.List;

public interface ItemDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemDO record);

    int insertSelective(ItemDO record);

    ItemDO selectByPrimaryKey(Integer id);
    List<ItemDO> listItem();

    int updateByPrimaryKeySelective(ItemDO record);

    int updateByPrimaryKey(ItemDO record);
    int increaseSales(@Param("id")Integer itemId,@Param("amount")Integer amount);
}