import com.rabbitmq.RabbitmqApplication;
import com.rabbitmq.client.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Created by fyw on 2020/5/31.
 */
@SpringBootTest(classes = RabbitmqApplication.class)
@RunWith(SpringRunner.class)
public class ProducerTest {

    /**
     * 1、原始的创建链接rabbitmq方式
     */
    @Test
    public void test() throws Exception{
        //1、创建一个链接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //2、获取一个链接
        Connection connection = connectionFactory.newConnection();
        //3、通过connectio链接创建一个通信（channel）信道
        Channel channel = connection.createChannel();
        //4、通过channel进行一系列操作
        /**
         * this.basicPublish(exchange, routingKey, false, props, body);
         * exchange 交换机(为空的话是按Fanout模式传输，直接匹配队列名称)
         * routingKey 绑定的路由键
         * props 属性
         * body 发送的具体消息
         */
        for(int i=0;i<5;i++){
            String msg = "发送的demo001的消息"+i;
            //channel.basicPublish("","demo_queue_001",null,msg.getBytes());
            channel.basicPublish("direc_exchange_demo_001","direct_demo_rout_001",null,msg.getBytes());
        }
        //5、从小到大关闭对应的链接
        channel.close();
        connection.close();

    }



    /**
     * 1、原始的创建链接rabbitmq方式
     */
    @Test
    public void testConfirnm() throws Exception{
        //1、创建一个链接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //2、获取一个链接
        Connection connection = connectionFactory.newConnection();
        //3、通过connectio链接创建一个通信（channel）信道
        Channel channel = connection.createChannel();
        //4、通过channel进行一系列操作
        //确定消息确认模式(同步的要添加监听器)
        channel.confirmSelect();


        /**
         * this.basicPublish(exchange, routingKey, false, props, body);
         * exchange 交换机(为空的话是按Fanout模式传输，直接匹配队列名称)
         * routingKey 绑定的路由键
         * props 属性
         * body 发送的具体消息
         */
        for(int i=0;i<5;i++){
            String msg = "发送的demo001的消息"+i;
            //channel.basicPublish("","demo_queue_001",null,msg.getBytes());
            channel.basicPublish("direc_exchange_demo","direct_demo_rout",null,msg.getBytes());
        }

        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                //l：代表了全局消息的唯一id，在发送消息的时候是要传过去的
                //失败进入的方法
                System.out.println("--------------No ack----------------");
            }

            @Override
            public void handleNack(long l, boolean b) throws IOException {
                //成功进入的方法
                System.out.println("--------------ack----------------");
            }
        });

        /**
         * mandatory：设置为true，如果为false，broker直接就删除了不可路由的消息
         * basicPublish(String exchange, String routingKey, boolean mandatory, boolean immediate, BasicProperties props, byte[] body)
         * mandatory : true
         *imrnediate 参数设为 true 时，如果交换器在将消息路由到队列时发现队列上并不存在 任何消费者，那么这条消息将不会存入队列中。
         *           当与路由键匹配的所有队列都没有消费者时 该消息会通过 Basic Return 返回至生产者。(不建议使用)
         */
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int i, String s, String s1, String s2, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                /**
                 * 参数说明按顺序
                 * replycode 响应码
                 * replyText 响应文本
                 * exchange 交换机
                 * routingKey 路由
                 * basicProperties 扩展属性
                 * bytes 消息体-具体的消息内容
                 */



                //代表消息找不到具体的路由队列，没有地方去的消息
                System.out.println("---------------------return---------------------");
            }
        });

    }


}
