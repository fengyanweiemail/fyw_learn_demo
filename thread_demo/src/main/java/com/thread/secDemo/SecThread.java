package com.thread.secDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by fyw on 2020/6/11.
 */
@Component
public class SecThread implements Runnable {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public void run() {


        //模拟
        try{
            //执行订单生成入库操作
            Thread.sleep(1000);
            //执行库存减1
            stringRedisTemplate.opsForValue().increment("count",-1);
            System.out.println(1111111111);
        }catch (Exception e){

        }

    }
}
