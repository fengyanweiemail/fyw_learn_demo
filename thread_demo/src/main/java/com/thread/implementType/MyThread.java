package com.thread.implementType;

import org.springframework.stereotype.Component;

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
}
