package com.thread.implementType;

import java.util.concurrent.Callable;

/**
 * Created by fyw on 2020/6/1.
 * 实现callable接口，有返回值
 */
public class MyCallAble implements Callable<String> {

    @Override
    public String call() throws Exception {

        return "MyCallAble线程执行了";
    }
}
