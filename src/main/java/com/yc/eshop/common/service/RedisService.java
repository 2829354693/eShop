package com.yc.eshop.common.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 余聪
 * @date 2020/12/3
 */
@Component
public class RedisService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void set(String key, Object value) {
        //更改在redis里面查看key编码问题
        RedisSerializer redisSerializer = new StringRedisSerializer();
        stringRedisTemplate.setKeySerializer(redisSerializer);
        stringRedisTemplate.setValueSerializer(redisSerializer);
        stringRedisTemplate.opsForValue().set(key, (String) value);
    }

    public void setExpire(String key, Object value, Integer hours) {
        //更改在redis里面查看key编码问题
        RedisSerializer redisSerializer = new StringRedisSerializer();
        stringRedisTemplate.setKeySerializer(redisSerializer);
        stringRedisTemplate.setValueSerializer(redisSerializer);
        stringRedisTemplate.opsForValue().set(key, (String) value, hours, TimeUnit.HOURS);
    }

    public Object get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

}
