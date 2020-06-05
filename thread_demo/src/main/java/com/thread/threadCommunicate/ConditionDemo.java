package com.thread.threadCommunicate;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fyw on 2020/6/5.
 */
public class ConditionDemo {
    int a = 10;
    ReentrantLock reentrantLock = new ReentrantLock();
    //通过lock获取 condition必须和ReentrantLock联合使用
    Condition condition = reentrantLock.newCondition();
    public void odd(){
        try{
            reentrantLock.lock();
            while (a>0){
                if(a%2!=0){
                    System.out.println(Thread.currentThread().getName()+"打印结果___"+a);
                    a--;
                    condition.signal();
                }else{
                    System.out.println(Thread.currentThread().getName()+"进入等待");
                    condition.await();
                }
            }
        }catch (Exception e){

        }finally {
            reentrantLock.unlock();
        }


        }

    public void event(){

        try{
            reentrantLock.lock();
            while (a>0){
                if(a%2==0){
                    System.out.println(Thread.currentThread().getName()+"打印结果___"+a);
                    a--;
                    condition.signal();
                }else{
                    System.out.println(Thread.currentThread().getName()+"进入等待");
                    condition.await();
                }
            }
        }catch (Exception e){

        }finally {
            reentrantLock.unlock();
        }
    }


    public static void main(String[] args) {
         ConditionDemo t = new ConditionDemo();
        Thread thread = new Thread(()->{
            t.odd();
        });
        //jdk 8 线程简化写法
        /**
         * ()代表参数
         * t.event();代表方法体，即执行的具体内容
         */
        Thread thread1 = new Thread(()->{
            t.event();
        });
        thread.start();
        thread1.start();
    }
}
