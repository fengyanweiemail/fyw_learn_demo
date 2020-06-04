package com.thread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fyw on 2020/5/28.
 */
@SpringBootApplication
public class ThreadApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThreadApplication.class,args);
    }
}
