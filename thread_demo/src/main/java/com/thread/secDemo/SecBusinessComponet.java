package com.thread.secDemo;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.*;

/**
 * Created by fyw on 2020/6/11.
 */
@Component
public class SecBusinessComponet {
    @Autowired
     RedisTemplate redisTemplate;


    @Autowired
    SecThread secThread;
    volatile int i = 1;
    static String SET = "REDIS_SET";
    public static String LIST = "REDIS_LIST";
    public static String MAP = "REDIS_MAP";
    /**
     * ThreadPoolExecutor(int corePoolSize,
     *                               int maximumPoolSize,
     *                               long keepAliveTime,
     *                               TimeUnit unit,
     *                               BlockingQueue<Runnable> workQueue)
     *     corePoolSize:核心线程数，不会被关闭，
     *     maximumPoolSize：最大线程数大于等于核心线程数，超过核心线程数部分，空闲就会被关闭
     *     keepAliveTime：超过核心线程数部分的生存时间
     *     unit： keepAliveTime的单位
     *     workQueue：LinkedBlockingQueue
     *
     */
    ThreadPoolExecutor executor = new ThreadPoolExecutor(10,30,300, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(1000));

    /**
     * 秒杀业务的demo（一旦后台返回无库存，直接按钮不能点击，降低服务压历）
     * 实现思路（依赖于redis，数据库，多线程）
     * 1、生成一个redis SET集合，用来放置已参与用户的-userId,如果有直接返回，避免走下面业务增加服务压力
     * 2、根据redis list集合判断是否还存在库存（存在的话直接取出来一个），存在往下走，不存在直接返回
     * 3、生成待支付订单进行入库（此处入库的时候，当然要有商品信息和用户信息，商品信息根据redis来查询，防止被篡改）
     * 4、第三步获取商品信息的时候在更新数据库对应的库存
     */
    public JSONObject secDemo(String userId, String goodId){
        System.out.println("进入方法"+i++);
        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject();
        }catch (Exception e){
            e.printStackTrace();
        }

       //如果存在
       Boolean b = redisTemplate.boundSetOps(SET).isMember(userId);
       if(b){

           System.out.println("存在未支付订单，或者已经领取过订单");
            jsonObject.put("code",1);
            jsonObject.put("msg","存在未支付订单，或者已经领取过订单");
            return jsonObject;
       }
       //判断库存
        String good = (String)redisTemplate.boundListOps(LIST).rightPop();
       if(StringUtils.isEmpty(good)){
           //没有库存
           System.out.println("已抢完");
           jsonObject.put("code",1);
           jsonObject.put("msg","已抢完");
           return jsonObject;
       }
        /**
         * 有库存，将用户信息放入redis,同时存储用户id和产品id的关联关系（Lua保证原子性操作）
         *
         * 此处也有为了线程能独立的去处理入库操作准备的意图
         */
        redisTemplate.boundSetOps(SET).add(userId);
       redisTemplate.boundHashOps(MAP).put(userId,goodId);

       //执行订单入库，数据库库存减1等操作，此处用多线程处理
        executor.execute(secThread);
        jsonObject.put("code",0);
        jsonObject.put("msg","抢购成功，赶紧去付款");
        System.out.println("抢购成功，赶紧去付款");
        System.out.println(redisTemplate.boundListOps("").range(0,-1));
        return jsonObject;
    }



}
