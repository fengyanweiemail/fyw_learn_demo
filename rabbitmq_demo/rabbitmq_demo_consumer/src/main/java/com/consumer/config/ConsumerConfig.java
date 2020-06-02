package com.consumer.config;

import com.consumer.MessageConsumer.MessageConsumerAdapter;
import com.consumer.MessageConsumer.MessageConsumerLisenter;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * Created by fyw on 2020/6/1.
 */
@Configuration
public class ConsumerConfig {

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
         connectionFactory.setAddresses("127.0.0.1:5672");
         connectionFactory.setVirtualHost("/");
         connectionFactory.setUsername("guest");
         connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    /**
     * 创建SimpleMessageListenerContainer容器
     * SimpleMessageListenerContainer容器对消费者配置项有很好的支持
     * 可以监听多个队列、自动启动、自动声明
     * 设置消息的签收模式、是否重回队列、handler异常捕获、消费者标签的生成策略、是否独占、消费者属性等等
     * 设置消息监听器、转换器等
     * 也可以动态的设置一些东西
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        /**
         * 进行一些消费者的设置
         */
        container.setQueueNames("springBean_queue","springBean_queue1");
        //设置最小最大的消费者个数
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        //设置消费者标签生成的策略
        container.setConsumerTagStrategy(new ConsumerTagStrategy(){
            @Override
            public String createConsumerTag(String queue) {
                return queue+"::"+ UUID.randomUUID().toString();
            }
        });
        //设置是否重回队列，一般都是false
        container.setDefaultRequeueRejected(false);
        //设置ack签收模式 设置为手动签收
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置最大未消费数量
        container.setPrefetchCount(1);
        //接收到消息做的第一件事
        //container.setAfterReceivePostProcessors();
        /**
         * 添加具体的消息监听类(第一种实现)
         */
        //container.setMessageListener(new MessageConsumerLisenter());
        /**
         * 第二种实现 (添加自己随便创建的类，但是要用适配器来适配)
         * 但是这种方法名是有规则的，可以自己指定，也可以用默认的，如下：
         * public static final String ORIGINAL_DEFAULT_LISTENER_METHOD = "handleMessage";
         * 或者直接指定public MessageListenerAdapter(Object delegate, String defaultListenerMethod)
         * defaultListenerMethod传入方法名称即可
         *
         * 适配器怎么手动签收？
         */
        MessageListenerAdapter messageAdapter = new MessageListenerAdapter(new MessageConsumerAdapter());
        //设置默认方法名
        messageAdapter.setDefaultListenerMethod("handleMessageStr");
        //添加消息转换器
        MessageConverter messageConverter = new MessageConverter() {
            /**
             *
             * @param o
             * @param messageProperties
             * @return
             * @throws MessageConversionException
             */
            @Override
            public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
                return new Message(o.toString().getBytes(),messageProperties);
            }

            /**
             * 接收到的参数转化
             * @param message
             * @return
             * @throws MessageConversionException
             */
            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                //可以根据头部的类型text json等做一个转化
                message.getMessageProperties().getContentType();
                return new String(message.getBody());
            }
        };
        /**
         * messageAdapter.setMessageConverter(null);
         * 设置为null的话，自定义里面可以用Message对象进行接收
         */
        messageAdapter.setMessageConverter(messageConverter);
        /**
         * messageAdapter.setQueueOrTagToMethodName();
         * 通过它设置队列名和方法名一一对应，实现同时多个方法监听多个队列
         */


        container.setMessageListener(messageAdapter);

        return container;
    }
}
