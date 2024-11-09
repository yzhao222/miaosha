package org.example.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.example.service.CacheService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService {
    private Cache<String,Object> commonCache=null;
    @PostConstruct
    //guava cache
    public void init(){
        commonCache= CacheBuilder.newBuilder()
                //设置缓存容器的初始容量
                .initialCapacity(10)
                //设置缓存中最多可以存储100个key，超过按照lru策略移除
                .maximumSize(100)
                //设置写缓存后过期时间
                .expireAfterWrite(60,TimeUnit.SECONDS).build();

    }
    @Override
    public void setCommonCache(String key, Object value) {
        commonCache.put(key,value);
    }

    @Override
    public Object getFromCommonCache(String key) {
        return commonCache.getIfPresent(key);
    }
}
