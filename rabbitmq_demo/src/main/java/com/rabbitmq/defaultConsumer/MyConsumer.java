package com.rabbitmq.defaultConsumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * Created by fyw on 2020/5/31.
 */
public class MyConsumer extends DefaultConsumer {
    Channel channel;
    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    /**
     *
     * @param consumerTag 消费者的唯一标签
     * @param envelope  路由键，消息标签、关键性信息等
     * @param properties 属性
     * @param body 具体的消息体
     * @throws IOException
     */
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("consumerTag:"+consumerTag);
        System.out.println("envelope:"+envelope);
        System.out.println("properties:"+properties);
        System.out.println("body:"+new String(body));

        /**
         * basicAck(long deliveryTag, boolean multiple)
         * deliveryTag 消息的唯一主键
         * multiple 是否批量ack
         *
         * basicNack(long deliveryTag, boolean multiple, boolean requeue);一般不适用，报错的话记录，正常的业务产生的nack容易造成无限循环
         * requeue 是否重回队列
         *
         * 需要注意的是，如果是因为网络即非正常原因造成的未返回ack，消息将会被转发到另一个consumer，但是如果网络正常通信，broker会一直等待ack的返回
         */
        channel.basicAck(envelope.getDeliveryTag(),false);

    }
}
