package com.thread.threadCommunicate;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by fyw on 2020/6/5.
 */
public class CyclicBarrierDemo {
    //3个线程，全部准备完毕再同时一下子执行
    /**
     * 如果只有小于3个线程执行了cyclicBarrier.await();，那么会无线等待下去
     * 3个里面都要cyclicBarrier.await();等到三个线程准备完毕了同时执行，可以用作并行（粗略上也可以叫并发）测试
     */
    CyclicBarrier cyclicBarrier = new CyclicBarrier(3);



    public void doC(){
        try {
            System.out.println(Thread.currentThread().getName()+"准备就绪111");
            System.out.println(Thread.currentThread().getName()+"___"+System.currentTimeMillis());
        }catch (Exception e){

        }
    }

    public void doC1(){
        try {
            //cyclicBarrier.await();
            System.out.println(Thread.currentThread().getName()+"准备就绪");
            System.out.println(Thread.currentThread().getName()+"___"+System.currentTimeMillis());

        }catch (Exception e){

        }
    }

    public void doC2(){
        try {
            Thread.sleep(1000);
            cyclicBarrier.await();

            System.out.println(Thread.currentThread().getName()+"准备就绪");
            System.out.println(Thread.currentThread().getName()+"___"+System.currentTimeMillis());

        }catch (Exception e){

        }
    }
    public void doC3(){
        try {
            Thread.sleep(1500);
            cyclicBarrier.await();
            System.out.println(Thread.currentThread().getName()+"都准备就绪了吗");

            System.out.println(Thread.currentThread().getName()+"___"+System.currentTimeMillis());

        }catch (Exception e){

        }
    }

    public static void main(String[] args) {
        CyclicBarrierDemo cyclicBarrierDemo = new CyclicBarrierDemo();
        Thread  thread = new Thread(()->{
            cyclicBarrierDemo.doC();
        });
        Thread  thread1 = new Thread(()->{
            cyclicBarrierDemo.doC1();
        });
        Thread  thread2 = new Thread(()->{
            cyclicBarrierDemo.doC2();
        });
        Thread  thread3 = new Thread(()->{
            cyclicBarrierDemo.doC3();
        });
        thread3.start();
        thread1.start();
        thread2.start();
        thread.start();
    }
}
