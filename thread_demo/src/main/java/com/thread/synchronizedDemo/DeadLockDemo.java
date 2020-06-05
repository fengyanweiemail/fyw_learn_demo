package com.thread.synchronizedDemo;

/**
 * Created by fyw on 2020/6/5.
 */
public class DeadLockDemo {

    private static Object o1 = new Object();
    private static Object o2 = new Object();
    /**
     * 死锁演示
     * 线程1启动调用lock1，获取o1静态对象锁，然后睡眠1s等待线程2启动获取对象2的锁，然后睡眠，
     * 睡眠结束后线程1
     *
     *
     *
     */
    public void lock1(){
        synchronized (o1){
            System.out.println(Thread.currentThread().getName()+"___111111111111");
            try{
                Thread.sleep(1000);
            }catch (Exception e){

            }
            synchronized (o2){
                System.out.println("___________22222222222222");
            }
            System.out.println("______3333333333333333");

        }
    }

    public void lock2(){
        //第一次获取自身对象锁
        synchronized (o2){
            System.out.println(Thread.currentThread().getName()+"___AAAAAAAA");
            try{
                Thread.sleep(1000);
            }catch (Exception e){

            }
            synchronized (o1){
                System.out.println("___________BBBBBBBBBBB");
            }
            System.out.println("______CCCCCCCCCCCCCC");
        }
    }

    public static void main(String[] args) {

        final DeadLockDemo d = new DeadLockDemo();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                d.lock1();
            }
        });
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                d.lock2();
            }
        });
        thread.start();
        thread1.start();
    }
}
