package org.example.service;

import org.example.service.model.PromoModel;

public interface PromoService {
    //根据itemId获取正在或即将开始的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);
    //活动发布
    void publishPromo(Integer promoId);
    //生成秒杀用的令牌
    String generateSecondKillToken(Integer promoId,Integer itemId,Integer userId);
}
