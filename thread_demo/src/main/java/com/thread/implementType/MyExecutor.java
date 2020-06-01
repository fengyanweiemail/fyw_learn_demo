package com.thread.implementType;

import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * Created by fyw on 2020/6/1.
 * Executors
 * 切记：局部创建的线程池，使用之后一定要关闭，一定，不然很可能造成线程泄露甚至内存溢出
 */
@Component
public class MyExecutor {
    ExecutorService pool = Executors.newFixedThreadPool(3);

    public void call() throws Exception{
        //创建计数器和线程数量相等(利用计数器判断)
        final CountDownLatch latch = new CountDownLatch(3);
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println(1);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    latch.countDown();//计数器减1
                }

            }
        });
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println(2);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    latch.countDown();//计数器减1
                }

            }
        });
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println(3);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    latch.countDown();//计数器减1
                }

            }
        });
        //线程内逻辑未执行完的话，最多等待3s
        latch.await(3, TimeUnit.SECONDS);
        System.out.println("线程池已经执行完毕了");

    }



    public String callV() throws Exception{


        Future<String> future1 = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "1";
            }
        });

        Future<String> future2 = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "2";
            }
        });
        final  String[] abc = new String[]{"1","2"} ;
        Future<String[]> future3 = pool.submit(new Runnable() {
            @Override
            public void run() {
                 abc[0] = "123";
            }
        },abc);
          return future1.get()+"---"+future2.get()+"----"+future3.get()[0]+"_---"+future3.get()[1];
    }

}
