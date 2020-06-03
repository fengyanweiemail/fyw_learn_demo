package com.boot.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Created by fyw on 2020/6/2.
 */
@Component
public class SendMsg {
    final CorrelationData correlationData = new CorrelationData();
    /**
     * 配置之后可以直接使用-rabbitTemplate
     * 相关原理可以研究下springboot的启动原理，会根据jar和配置在启动是默认实现
     */
    @Autowired
    RabbitTemplate rabbitTemplate;


    public void send(Object msg, Map<String,Object> perproties){
        MessageHeaders messageHeaders = new MessageHeaders(perproties);
        Message message = MessageBuilder.createMessage(msg,messageHeaders);
        //设置发送消息的唯一性id，线程安全的话可以加个代码块
        correlationData.setId(UUID.randomUUID().toString());
        //添加对应的可靠性发送和不可路由的callback
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);


        //rabbitTemplate.convertAndSend("springboot_exchange","springboot.abc",message,correlationData);
        rabbitTemplate.convertAndSend("springboot_anno_exchange","springboot.abc",message,correlationData);
    }

    /**
     * 消息发送的确认模式
     */
    final RabbitTemplate.ConfirmCallback  confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean b, String s) {
            if(!b){
                System.out.println("记录错误日志");
            }
            System.out.println(correlationData.getId());
        }
    };
    /**
     * 不可路由消息接收
     */
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int i, String s, String s1, String s2) {
            System.out.println(new String(message.getBody()));
        }
    };

}
