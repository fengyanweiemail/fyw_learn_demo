package com.consumer.MessageConsumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

/**
 * Created by fyw on 2020/6/1.
 */
public class MessageConsumerLisenter implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println(new String(message.getBody()));
        System.out.println(message.getMessageProperties().getConsumerQueue());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
