package org.example.service;

import org.example.error.BusinessException;
import org.example.service.model.ItemModel;

import java.util.List;

public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
    //商品列表浏览
    List<ItemModel> listItem();
    //商品详情浏览
    ItemModel getItemById(Integer id);
    //库存扣减
    boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;
    //回滚库存
    boolean increaseStock(Integer itemId, Integer amount) throws BusinessException;
    //异步更新库存
    boolean asyncDecreaseStock(Integer itemId, Integer amount);
    //初始化库存流水
    String initStockLog(Integer itemId,Integer amount);
    //商品销量增加
    void increaeSales(Integer itemId, Integer amount) throws BusinessException;
    //验证item及promo是否有效
    ItemModel getItemByIdInCache(Integer id);

}
