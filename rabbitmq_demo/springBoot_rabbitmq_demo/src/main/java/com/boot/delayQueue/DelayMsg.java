package com.boot.delayQueue;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * Created by fyw on 2020/6/3.
 */
@Component
public class DelayMsg {
    /**
     * 插件方式实现，延迟消息发送
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "delay_queue_test",durable = "true",autoDelete = "false"),
            exchange = @Exchange(value = "delay_exchange_test",
                    durable = "true",autoDelete = "false",type = "topic",
                    ignoreDeclarationExceptions = "true",delayed = "true"),
            key = "delay.#"
    ))
    @RabbitHandler
    public void onMsg(Message message, Channel channel) throws Exception{
        //message.getPayload() 获得的直接是消息的泛型
        System.out.println(message.getPayload());
        System.out.println(message.getHeaders().get("abc"));
        channel.basicAck((Long)(message.getHeaders().get(AmqpHeaders.DELIVERY_TAG)),false);
    }
}
