package com.thread.threadCommunicate;

/**
 * Created by fyw on 2020/6/5.
 * 线程通讯测试类
 */
public class ObjectDemo {
    private static int  a = 10;
    Object object = new Object();
    /**
     * 奇数
     * object.wait() 、object.notify() 、object.notifyAll() 必须在synchronized内使用，
     * 且synchronized (object)锁对象，必须为new 的object
     *
     * 执行结果分析
     * Thread-1首相抢占资源，然后进入odd的else方法 ，进入等待释放object锁，
     * 然后Thread-0抢占到资源，进入if，打印结果，并唤醒Thread-1，但是此时Thread-0
     * 并没有释放锁，因此Thread-1只能继续等待，然后Thread-0继续运行到else，进行等待并
     * 释放锁，之后Thread-1得到锁进入if打印结果，同上又循环进入else等待并释放锁，然后Thread-0得到锁
     * 接着else往下执行，打印结果，再循环进入if，然后如此循环下去
     *
     * Thread-1打印结果___10
     * Thread-1进入等待2222
     * Thread-0进入等待
     * Thread-0打印结果___9
     * Thread-0进入等待111
     * Thread-1进入等待
     * Thread-1打印结果___8
     * Thread-1进入等待2222
     * Thread-0进入等待
     * Thread-0打印结果___7
     * Thread-0进入等待111
     * Thread-1进入等待
     * Thread-1打印结果___6
     * Thread-1进入等待2222
     * Thread-0进入等待
     * Thread-0打印结果___5
     * Thread-0进入等待111
     * Thread-1进入等待
     * Thread-1打印结果___4
     * Thread-1进入等待2222
     * Thread-0进入等待
     * Thread-0打印结果___3
     * Thread-0进入等待111
     * Thread-1进入等待
     * Thread-1打印结果___2
     * Thread-1进入等待2222
     * Thread-0进入等待
     * Thread-0打印结果___1
     * Thread-0进入等待111
     * Thread-1进入等待
     *
     */
    public void odd(){

        synchronized (object){
            while (a>0){
                if(a%2!=0){
                    System.out.println(Thread.currentThread().getName()+"打印结果___"+a);
                    a--;
                    try {
                        //唤醒另一个需要此对象锁的线程
                        object.notify();
                    }catch (Exception e){

                    }
                    //不会打印
                    System.out.println(Thread.currentThread().getName()+"进入等待111");
                }else{
                    try {
                        object.wait();//非奇数进入等待状态
                    }catch (Exception e){

                    }
                    //不会打印
                    System.out.println(Thread.currentThread().getName()+"进入等待");
                }
            }
        }
    }

    public void event(){

        synchronized (object) {
            while (a>0) {
                if (a % 2 == 0) {
                    System.out.println(Thread.currentThread().getName() + "打印结果___" +a);
                    try {
                        a--;
                        object.notify();//唤醒另一个需要此对象锁的线程
                    } catch (Exception e) {

                    }
                    //不会打印
                    System.out.println(Thread.currentThread().getName()+"进入等待2222");
                } else {
                    try {
                        object.wait(10000);//唤醒另一个需要此对象锁的线程
                    } catch (Exception e) {

                    }
                    //不会打印
                    System.out.println(Thread.currentThread().getName()+"进入等待");
                }
            }
        }
    }


    public static void main(String[] args) {
        final ObjectDemo t = new ObjectDemo();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                t.odd();
            }
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
