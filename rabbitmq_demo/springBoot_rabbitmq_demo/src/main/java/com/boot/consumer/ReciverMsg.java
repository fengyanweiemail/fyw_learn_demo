package com.boot.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Created by fyw on 2020/6/2.
 */
@Component
public class ReciverMsg {

    /**
     * 默认情况下，当任何异常发生时，RabbitAdmin将立即停止处理所有声明;这可能会导致下游问题——例如，由于没有声明另一个队列(在错误队列之后定义的)，侦听器容器无法初始化。
     * 可以通过将RabbitAdmin上的ignore-declaration-exceptions属性设置为true来修改此行为。这个选项指示RabbitAdmin记录异常，并继续声明其他元素。当使用java配置RabbitAdmin时，这个属性是ignoredeclarationexception。这是一个全局设置，适用于所有元素、队列、交换器和绑定，具有一个类似的属性，仅适用于这些元素。
     *
     * 在1.6版本之前，此属性仅在通道上发生IOException时(例如当前属性与所需属性不匹配时)才生效。现在，该属性将对任何异常生效，包括TimeoutException等。
     *
     * 此外，任何声明异常都会导致DeclarationExceptionEvent的发布，该事件是一个ApplicationEvent，上下文中的任何ApplicationListener都可以使用该事件。事件包含对admin的引用、正在声明的元素和可抛出的元素。
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "springboot_anno_queue",durable = "true",autoDelete = "false"),
            exchange = @Exchange(value = "springboot_anno_exchange",
                    durable = "true",autoDelete = "false",type = "topic",
            ignoreDeclarationExceptions = "true"),
            key = "springboot.#"
    ))
    @RabbitHandler
    public void onMsg(Message message, Channel channel) throws Exception{
        //message.getPayload() 获得的直接是消息的泛型
        System.out.println(message.getPayload());
        System.out.println(message.getHeaders().get("abc"));
        channel.basicAck((Long)(message.getHeaders().get(AmqpHeaders.DELIVERY_TAG)),false);
    }

    /**
     * message 分为两部分 payload和headers
     * @Payload 实体类（发送的话一定要序列化）
     * @Header Map<String,Object>接收headers
     *
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "springboot_anno_queue1",durable = "true",autoDelete = "false"),
            exchange = @Exchange(value = "springboot_anno_exchange",
                    durable = "true",autoDelete = "false",type = "topic",
                    ignoreDeclarationExceptions = "true"),
            key = "springboot1.#"
    ))
    @RabbitHandler
    public void onMsg1(Message message,  Channel channel) throws Exception{
        //message.getPayload() 获得的直接是消息的泛型
        System.out.println(message.getPayload());
        System.out.println(message.getHeaders().get("abc"));
        channel.basicAck((Long)(message.getHeaders().get(AmqpHeaders.DELIVERY_TAG)),false);
    }
}
