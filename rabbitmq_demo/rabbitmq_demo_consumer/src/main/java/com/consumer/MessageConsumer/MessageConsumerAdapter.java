package com.consumer.MessageConsumer;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by fyw on 2020/6/1.
 */
public class MessageConsumerAdapter {
    @Autowired
    ConnectionFactory connectionFactory;
    public void handleMessage(byte[] messsagebody){
        System.out.println(new String(messsagebody));
    }



    public void handleMessageStr(String messsagebody){
        System.out.println(messsagebody);
    }
}
