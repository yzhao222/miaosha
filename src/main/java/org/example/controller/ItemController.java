package org.example.controller;

import org.example.controller.viewobject.ItemVO;
import org.example.error.BusinessException;
import org.example.response.CommonReturnType;
import org.example.service.CacheService;
import org.example.service.ItemService;
import org.example.service.PromoService;
import org.example.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller("item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials="true", allowedHeaders ="*", origins = "*")
public class ItemController extends BaseController{
    @Autowired
    private ItemService itemService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private PromoService promoService;
    @RequestMapping(value = "/create",method ={ RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name="title")String title,
                                       @RequestParam(name="description")String description,
                                       @RequestParam(name="price")BigDecimal price,
                                       @RequestParam(name="imgUrl")String imgUrl,
                                       @RequestParam(name="stock")Integer stock) throws BusinessException {
        //封装service请求用来创建商品
        ItemModel itemModel=new ItemModel();
        itemModel.setTitle(title);
        itemModel.setStock(stock);
        itemModel.setPrice(price);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);
        ItemModel itemModelForReturn=itemService.createItem(itemModel);
        ItemVO itemVO=convertVOFromModel(itemModelForReturn);
        return CommonReturnType.create(itemVO);
    }
    //商品详情页浏览
    @RequestMapping(value = "/get",method ={ RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name="id")Integer id) throws BusinessException {
        ItemModel itemModel=null;
        //本地缓存获取
        itemModel= (ItemModel) cacheService.getFromCommonCache("item_"+id);

        if (itemModel == null) {
            //根据商品id到redis中获取
            itemModel=(ItemModel) redisTemplate.opsForValue().get("item_"+id);
            //若redis不存在则访问数据库
            if(itemModel==null){
                itemModel=itemService.getItemById(id);
                //设置到redis内
                redisTemplate.opsForValue().set("item_"+id,itemModel);
                redisTemplate.expire("item_"+id,10, TimeUnit.MINUTES);
            }
            //设置到本地缓存
            cacheService.setCommonCache("item_"+id,itemModel);
        }
        //封装service请求用来创建商pin
        ItemVO itemVO=convertVOFromModel(itemModel);
        return CommonReturnType.create(itemVO);
    }
    //商品列表页浏览
    @RequestMapping(value = "/list",method ={ RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem () throws BusinessException {
        //封装service请求用来创建商品
        List<ItemModel> itemModelList=itemService.listItem();
        List<ItemVO> itemVOList=itemModelList.stream().map(itemModel -> {
            ItemVO itemVO=this.convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return  CommonReturnType.create(itemVOList);
    }

    @RequestMapping(value = "/publishpromo",method ={ RequestMethod.GET})
    @ResponseBody
    public CommonReturnType publishpromo(@RequestParam(name="id")Integer id){
        promoService.publishPromo(id);
        return CommonReturnType.create(null);
    }
    private ItemVO convertVOFromModel(ItemModel itemModel){
        if(itemModel==null){
            return null;
        }
        ItemVO itemVO=new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        if(itemModel.getPromoModel()!=null){
            //有着正在进行的秒杀活动
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setStartDate(itemModel.getPromoModel().getStartData().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else {
            itemVO.setPromoStatus(0);
        }
        return  itemVO;
    }
}
