package com.producer.config;

import com.producer.send.SendMessage;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.PendingConfirm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by fyw on 2020/5/31.
 * 此类模拟spring利用springAmqp与RabbitMq的整合方式
 *
 *
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("127.0.0.1:5672");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //开启消息确认模式
        connectionFactory.setPublisherConfirms(true);
        //开启不可路由消息的接收
        connectionFactory.setPublisherReturns(true);
        return  connectionFactory;
    }

    /**
     * 在spring很好的操作RabbitMq（autoStartup必须设置为true，
     * RabbitAdmin会从spring容器中获取对应的exchange、queue等，
     * 然后使用RabbitTemplate的execute方法进行创建、修改、删除）
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 队列、交换机、绑定的声明
     *
     * 在使用的时候会创建
     */
    @Bean
    public Queue queue(){
        return new Queue("springBean_queue1",true);
    }
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange("springBean_exchange1",true,false);
    }
    @Bean
    public Binding binding(){
        return new Binding("springBean_queue1", Binding.DestinationType.QUEUE,"springBean_exchange1","springBean.*",null);
    }

    /**
     * 消息接收发送模板
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //注入实现1（最好写在这里，注解单例的）
        //rabbitTemplate.setConfirmCallback(new SendMessage());
        //rabbitTemplate.setReturnCallback(new SendMessage());
        //添加returnLisenter的时候此处必须设置为true(mandatory：设置为true，如果为false，broker直接就删除了不可路由的消息)
        //rabbitTemplate.setMandatory(true);
        //这是一种写法
        /*rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback(){
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack) {
                    System.out.println("消息确认成功");
                } else {
                    //处理丢失的消息（nack）
                    System.out.println("消息确认失败");
                }
            }
        });*/
        return rabbitTemplate;
    }


}
