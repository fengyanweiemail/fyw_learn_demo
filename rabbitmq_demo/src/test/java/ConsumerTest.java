import com.rabbitmq.RabbitmqApplication;
import com.rabbitmq.client.*;
import com.rabbitmq.defaultConsumer.MyConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by fyw on 2020/5/31.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitmqApplication.class)
public class ConsumerTest {

    //@Test
    public  void test() throws Exception{
        //1、获取一个链接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //是否支持重连，
        connectionFactory.setAutomaticRecoveryEnabled(true);
        //每3s中进行重连一次
        connectionFactory.setConnectionTimeout(3000);
        //2、创建一个链接
        Connection connection = connectionFactory.newConnection();
        //3、通过链接获取对应的通信信道channel
        Channel channel = connection.createChannel();
        //4、通过channel操作
        /**
         * 声明一个交换机
         * (exchangeDeclare(String exchange, String type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments))
         * exchange 交换机名称
         * type 交换机类型
         * durable 是否持久化
         * autoDelete 是否自动删除（没有任何队列绑定的话会自动删除）
         * internal 是否为rabbitmq内部使用（一般设置为false）
         * arguments 扩展参数
         */
        String directName= "direc_exchange_demo";
        channel.exchangeDeclare(directName,"direct",true,false,false,null);
        /**
         * 声明一个队列(queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) )
         * queue 队列名称
         * durable 是否持久化队列，持久化-服务重启依然存在
         * exclusive 独占模式顺序执行，要求队列里面的消息仅能在这一个信道里面执行（为了保证消息的消费顺序性）
         * autoDelete 是否删除队列（如果发现这个队列没什么绑定啦、消息了，就是没什么用了，就直接删除了）
         * arguments 扩展参数
         */

        String queueName = "demo_queue_001";
        channel.queueDeclare(queueName,true,false,false,null);
        /**
         * 创建一个绑定关系
         * queueBind(String queue, String exchange, String routingKey, Map<String, Object> arguments)
         *
         */
        channel.queueBind(queueName,directName,"direct_demo_rout",null);
        /**
         * 创建一个消费者(这种方式已经淘汰了)
         */
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        /**
         * 设置channel信息 (basicConsume(String queue, boolean autoAck, Consumer callback))
         * queue 队列名称
         * autoAck 是否自动签收（一般项目里面要设置成非自动签收（限流））
         * callback 具体的消费者实例
         */
        channel.basicConsume(queueName,true,queueingConsumer);
        //5、获取消息
        while (true){
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("具体的消息：   "+msg);
            Envelope envelope = delivery.getEnvelope();
            System.out.println("消息的一些具体关联属性：   "+envelope.toString());
        }


    }


    @Test
    public  void testMyConsumer() throws Exception{
        //1、获取一个链接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //是否支持重连，
        connectionFactory.setAutomaticRecoveryEnabled(true);
        //每3s中进行重连一次
        connectionFactory.setConnectionTimeout(3000);
        //2、创建一个链接
        Connection connection = connectionFactory.newConnection();
        //3、通过链接获取对应的通信信道channel
        Channel channel = connection.createChannel();
        //4、通过channel操作
        /**
         * 声明一个交换机
         * (exchangeDeclare(String exchange, String type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments))
         * exchange 交换机名称
         * type 交换机类型
         * durable 是否持久化
         * autoDelete 是否自动删除（没有任何队列绑定的话会自动删除）
         * internal 是否为rabbitmq内部使用（一般设置为false）
         * arguments 扩展参数
         */
        String directName= "direc_exchange_demo_001";
        channel.exchangeDeclare(directName,"direct",true,false,false,null);
        /**
         * 声明一个队列(queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) )
         * queue 队列名称
         * durable 是否持久化队列，持久化-服务重启依然存在
         * exclusive 独占模式顺序执行，要求队列里面的消息仅能在这一个信道里面执行（为了保证消息的消费顺序性）
         * autoDelete 是否删除队列（如果发现这个队列没什么绑定啦、消息了，就是没什么用了，就直接删除了）
         * arguments 扩展参数
         */

        String queueName = "demo_queue_002";
        channel.queueDeclare(queueName,true,false,false,null);
        /**
         * 创建一个绑定关系
         * queueBind(String queue, String exchange, String routingKey, Map<String, Object> arguments)
         *
         */
        channel.queueBind(queueName,directName,"direct_demo_rout_001",null);
        /**
         * 创建一个消费者(这种方式已经淘汰了)
         */
        //QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        /**
         * 设置channel信息 (basicConsume(String queue, boolean autoAck, Consumer callback))
         * queue 队列名称
         * autoAck 是否自动签收（一般项目里面要设置成非自动签收（限流））
         * callback 具体的消费者实例
         */
        channel.basicConsume(queueName,true,new MyConsumer(channel));


    }


    @Test
    public  void testQOS() throws Exception{
        //1、获取一个链接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //是否支持重连，
        connectionFactory.setAutomaticRecoveryEnabled(true);
        //每3s中进行重连一次
        connectionFactory.setConnectionTimeout(3000);
        //2、创建一个链接
        Connection connection = connectionFactory.newConnection();
        //3、通过链接获取对应的通信信道channel
        Channel channel = connection.createChannel();
        //4、通过channel操作
        /**
         * 声明一个交换机
         * (exchangeDeclare(String exchange, String type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments))
         * exchange 交换机名称
         * type 交换机类型
         * durable 是否持久化
         * autoDelete 是否自动删除（没有任何队列绑定的话会自动删除）
         * internal 是否为rabbitmq内部使用（一般设置为false）
         * arguments 扩展参数
         */
        String directName= "direc_exchange_demo_001";
        channel.exchangeDeclare(directName,"direct",true,false,false,null);
        /**
         * 声明一个队列(queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) )
         * queue 队列名称
         * durable 是否持久化队列，持久化-服务重启依然存在
         * exclusive 独占模式顺序执行，要求队列里面的消息仅能在这一个信道里面执行（为了保证消息的消费顺序性）
         * autoDelete 是否删除队列（如果发现这个队列没什么绑定啦、消息了，就是没什么用了，就直接删除了）
         * arguments 扩展参数
         */

        String queueName = "demo_queue_002";
        channel.queueDeclare(queueName,true,false,false,null);
        /**
         * 创建一个绑定关系
         * queueBind(String queue, String exchange, String routingKey, Map<String, Object> arguments)
         *
         */
        channel.queueBind(queueName,directName,"direct_demo_rout_001",null);
        /**
         * 创建一个消费者(这种方式已经淘汰了)
         */
        //QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        /**
         * 设置channel信息 (basicConsume(String queue, boolean autoAck, Consumer callback))
         * queue 队列名称
         * autoAck 是否自动签收（一般项目里面要设置成非自动签收（限流设置为false））
         * callback 具体的消费者实例
         */
        channel.basicConsume(queueName,false,new MyConsumer(channel));
        /**
         * basicQos(int prefetchSize, int prefetchCount, boolean global)
         * prefetchSize 消息大小限制 0 不限制
         * prefetchCount 最大未确认数 设置为1就行
         * global true在channel级别做限制 false在consumer做限制，一般为false
         */
        channel.basicQos(0,1,false);

        /**
         * basicAck(long deliveryTag, boolean multiple)
         * deliveryTag 消息的唯一主键(代表的是消费的消息的唯一性id，隶属于channel，用来ack)
         * multiple 是否批量签收
         *
         * 应该写在消费者里面
         *
         * channel.basicAck("111",);
         */


    }
}
