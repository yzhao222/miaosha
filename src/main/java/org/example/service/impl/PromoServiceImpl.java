package org.example.service.impl;

import org.example.dao.PromoDOMapper;
import org.example.dataobject.PromoDO;
import org.example.error.BusinessException;
import org.example.error.EmBusinessError;
import org.example.service.ItemService;
import org.example.service.UserService;
import org.example.service.model.ItemModel;
import org.example.service.model.PromoModel;
import org.example.service.PromoService;
import org.example.service.model.UserModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    private PromoDOMapper promoDOMapper;
    @Autowired
    private ItemService itemService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        //获取对应商品的秒杀活动信息
        PromoDO promoDO=promoDOMapper.selectByItemId(itemId);
        PromoModel promoModel=convertFromDataObject(promoDO);
        //判断当前活动状态
        DateTime now =new DateTime();
        if(promoModel==null){
            return  null;
        }
        if(promoModel.getStartData().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else {
            promoModel.setStatus(2);
        }
        return promoModel;
    }

    @Override
    public void publishPromo(Integer promoId) {
        //通过活动id获取活动
        PromoDO promoDO=promoDOMapper.selectByPrimaryKey(promoId);
        if(promoDO.getItemId()==null || promoDO.getItemId()==0){
            return;
        }
        ItemModel itemModel=itemService.getItemById(promoDO.getItemId());
        //将库存同步到redis内
        redisTemplate.opsForValue().set("promo_item_stock_"+itemModel.getId(),itemModel.getStock());
        //将大闸的限制数字设到redis中
        redisTemplate.opsForValue().set("promo_door_count_"+promoId,itemModel.getStock().intValue()*5);
    }

    @Override
    public String generateSecondKillToken(Integer promoId,Integer itemId, Integer userId) {
        //判断是否库存已售罄,若key存在则直接返回下单失败
        if(redisTemplate.hasKey("promo_item_stock_invalid_"+itemId)){
            return null;
        }
        PromoDO promoDO=promoDOMapper.selectByPrimaryKey(promoId);
        PromoModel promoModel=convertFromDataObject(promoDO);
        //判断当前活动状态
        DateTime now =new DateTime();
        if(promoModel==null){
            return  null;
        }
        if(promoModel.getStartData().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else {
            promoModel.setStatus(2);
        }
        if(promoModel.getStatus().intValue()!=2){
            return null;
        }
        //商品是否存在，用户是否合法
        ItemModel itemModel=itemService.getItemByIdInCache(itemId);
        if(itemModel==null){
            return null;
        }
        UserModel userModel=userService.getUserByIdInCache(userId);
        if(userModel==null){
            return null;
        }
        //获取秒杀大闸的count数量
        long result=redisTemplate.opsForValue().increment("promo_item_stock_"+promoId,-1);
        if(result<0) {
            return null;
        }
        //生成token存入redis内
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set("promo_token_" + promoId + "_userid_" + userId + "_itemid_" + itemId, token);
        redisTemplate.expire("promo_token_" + promoId + "_userid_" + userId + "_itemid_" + itemId, 5, TimeUnit.MINUTES);
        return token;

    }

    private PromoModel convertFromDataObject(PromoDO promoDO){
        if(promoDO==null){
            return null;
        }
        PromoModel promoModel= new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));
        promoModel.setStartData(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return promoModel;
    }
}
