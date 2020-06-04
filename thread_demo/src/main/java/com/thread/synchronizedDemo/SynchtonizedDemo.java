package com.thread.synchronizedDemo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fyw on 2020/6/4.
 */
public  class SynchtonizedDemo implements Runnable{
    //共享变量
      int stock = 10;

    @Override
    public void run() {
        subStock3();

    }

    /**
     * public synchronized void subStock() 直接在方法上面写synchronized，默认获取的对象锁是自己本身类的对象实例,相当于 synchronized(new SynchtonizedDemo())
     * public static synchronized void subStock() 相当于 synchronized(SynchtonizedDemo.class)
     * 不加锁的情况：
     * 线程的名称:线程5------剩余的数量：0
     * 线程的名称:线程1------剩余的数量：-1
     * 线程的名称:线程2------剩余的数量：-2
     * 线程的名称:线程3------剩余的数量：-2
     *
     *
     *
     * 同步方法，对于方法里面的其他线程安全的业务逻辑也要等待，太影响效率;另外如果一个对象里面有多个同步的方法，那么多个方法
     * 之间会根据锁顺序执行
     *
     * demo1
     * 同步方法
     */
    public  synchronized void subStock() {
        System.out.println("1111");
            while (true){
                try {
                    //模拟业务逻辑处理
                    Thread.sleep(500);
                }catch (Exception e){

                }
                if(stock>0){
                    System.out.println("线程的名称:"+Thread.currentThread().getName()+"------剩余的数量："+stock--);

                }
            }
    }

    /**
     * demo2
     * 同步代码块
     * synchronized(SynchtonizedDemo.class) 类锁
     */
    public  void subStock2() {
        System.out.println("1111");
        synchronized(this){
            while (stock>0){
                try {
                    //模拟业务逻辑处理
                    Thread.sleep(500);
                }catch (Exception e){

                }

                System.out.println("线程的名称:"+Thread.currentThread().getName()+"------剩余的数量："+stock--);
            }
        }
    }

    /**
     * demo3
     * 同步代码块
     * reentrantLock锁
     * reentrantLock.lock();上锁
     * reentrantLock.unlock(); 手动解锁
     * 一定要一对的使用，最后必须执行unlock，不然造成死锁，所以一般将unlock放在finally里面
     */
      Lock reentrantLock = new ReentrantLock(true);
    public  void subStock3() {
        while (true){
            try {
                reentrantLock.lock();
                //模拟业务逻辑处理
                Thread.sleep(500);

                if(stock>0){
                    System.out.println("线程的名称:"+Thread.currentThread().getName()+"------剩余的数量："+stock--);
                }

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                reentrantLock.unlock();
            }

        }
    }



    public static void main(String[] args) {
        /*for(int i=0;i<10;i++){
            SynchtonizedDemo synchtonizedDemo =  new SynchtonizedDemo();
            Thread thread = new Thread(synchtonizedDemo,"线程"+i);
            thread.start();
        }*/
        SynchtonizedDemo synchtonizedDemo =  new SynchtonizedDemo();
        Thread thread = new Thread(synchtonizedDemo,"线程1");
        Thread thread2 = new Thread(synchtonizedDemo,"线程2");
        Thread thread3 = new Thread(synchtonizedDemo,"线程3");
        thread.start();
        thread2.start();
        thread3.start();
    }

}
