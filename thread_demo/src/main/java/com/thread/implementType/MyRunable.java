package com.thread.implementType;

import org.springframework.stereotype.Component;

/**
 * Created by fyw on 2020/6/1.
 */
public class MyRunable implements Runnable {
    @Override
    public void run() {
        for(int i=0;i<5;i++){
            System.out.println("MyRunable线程执行了_____"+i);
        }
    }
}
