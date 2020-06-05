package com.thread.implementType;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by fyw on 2020/6/1.
 * 继承Thread实现
 */
@Component
public class MyThread extends Thread{
    @Override
    public void run() {
        for(int i=0;i<5;i++){
            System.out.println("myThread执行了_____"+i);
        }
    }



    static Map<String, String[]> giftForCouponVipNNew = new HashMap<String, String[]>();
    static {giftForCouponVipNNew.put("2020061",new String[]{"gsx","hldc","mgtv"});
        giftForCouponVipNNew.put("2020065",new String[]{"gnzc","fdhy","mgsp"});
        giftForCouponVipNNew.put("2020062",new String[]{"xmly"});}
    public static void main(String[] args) {




         for(int i = 0;i<10000;i++){
            String  prizeCouponId1 = giftForCouponVipNNew.get("2020061")[ThreadLocalRandom.current().nextInt(giftForCouponVipNNew.get("2020061").length)];
            String  prizeCouponId3 = giftForCouponVipNNew.get("2020065")[ThreadLocalRandom.current().nextInt(giftForCouponVipNNew.get("2020065").length)];
            String  prizeCouponId4 = giftForCouponVipNNew.get("2020062")[ThreadLocalRandom.current().nextInt(giftForCouponVipNNew.get("2020062").length)];

             System.out.println("首签模拟奖品结果："+prizeCouponId1);
             System.out.println("3签模拟奖品结果："+prizeCouponId3);
             System.out.println("7签模拟奖品结果："+prizeCouponId4);
         }

    }
}
