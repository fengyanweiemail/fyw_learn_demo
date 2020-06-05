package com.thread.threadCommunicate;

import java.util.concurrent.Semaphore;

/**
 * Created by fyw on 2020/6/5.
 * 针对并发量大资源少的业务就行使用，比如100个人用2台电脑
 */
public class SemaphoreDemo {
    //同时允许最多2个线程执行指定的代码块，即同时允许一次2个人用台电脑，一次放行2个
    //Semaphore(2,false)true公平的，默认应该是false
    Semaphore semaphore = new Semaphore(2);

    /**
     * semaphore.acquire()此方法放入几个
     * semaphore.release()就要释放几个
     * 另外
     */
    public void use(){
        try{
            semaphore.acquire();//放行后关闭
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName()+"使用电脑");
            Thread.sleep(2000);
            semaphore.release();//运行结束释放
        }catch (Exception e){

        }

    }

    public static void main(String[] args) {
        SemaphoreDemo semaphoreDemo = new SemaphoreDemo();
        for(int i=0;i<100;i++){
            new Thread(()->{
                semaphoreDemo.use();
            }).start();
        }
    }
}
