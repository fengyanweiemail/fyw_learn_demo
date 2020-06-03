package com.boot.delayQueue;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Created by fyw on 2020/6/3.
 */
@Component
public class DelayP {
    @Autowired
    RabbitTemplate rabbitTemplate;
    final CorrelationData correlationData = new CorrelationData();

    public void send(Object msg, Map<String,Object> perproties){

        final  Integer i = Integer.valueOf(perproties.get("x-delay").toString());
        MessageHeaders messageHeaders = new MessageHeaders(perproties);
        Message message = MessageBuilder.createMessage(msg,messageHeaders);
        //设置发送消息的唯一性id，线程安全的话可以加个代码块
        correlationData.setId(UUID.randomUUID().toString());
        /**
         * MessagePostProcessor可以在消息发送前增加一些额外的东西
         */
        rabbitTemplate.convertAndSend("delay_exchange_test", "delay.abc", message, new MessagePostProcessor() {
            @Override
            public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message) throws AmqpException {
                message.getMessageProperties().setDelay(i);
                return message;
            }
        }, correlationData);
    }
}
