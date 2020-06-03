package com.producer.send;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by fyw on 2020/5/31.
 */
@Component
public class SendMessage implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback{
    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 实现2
     * 将SendMessage 确认监听器和不可路由监听器在方法使用之前注入到注入到rabbitTemplate
     */
    /*@PostConstruct
    public void init(){
        rabbitTemplate.setReturnCallback(this);
        rabbitTemplate.setConfirmCallback(this);
    }*/


    public void sendMessage(){
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("type","新消息");
        Message message = new Message("Hello World RabbitMq".getBytes(),messageProperties);
        /**
         * new CorrelationData("123") 消息的唯一主键
         */
        //实现3，总结只要在rabbitTemplate发送消息之前设置进去就行
        rabbitTemplate.setReturnCallback(this);
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.convertAndSend("springBean_exchange1", "springBean.abc", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //对发送的消息再做一次封装
                message.getMessageProperties().getHeaders().put("abc","abc");
                return message;
            }
        },new CorrelationData(UUID.randomUUID().toString()));



    }


    public void sendMessage11(){
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("type","新消息");
        Message message = new Message("Hello World RabbitMq111".getBytes(),messageProperties);
        /**
         * new CorrelationData("123") 消息的唯一主键（代表的是发送消息的唯一性id，用来confirm）
         */
        rabbitTemplate.convertAndSend("springBean_exchange1", "springBean123.abc", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //对发送的消息再做一次封装
                message.getMessageProperties().getHeaders().put("abc","abc");
                return message;
            }
        },new CorrelationData(UUID.randomUUID().toString()));



    }

    /**
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String msgId = correlationData.getId();

        if (ack){
            System.out.println("消息唯一主键-----"+msgId);
        }else {
            System.out.println("失败原因----"+cause);
        }
    }

    /**
     * returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey)
     * @param message
     * @param i
     * @param s
     * @param s1
     * @param s2
     */
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        System.out.println("11111111111"+new String(message.getBody())+"--"+i+"---"+s+"-----"+s1+"-----"+s2);
    }
}
