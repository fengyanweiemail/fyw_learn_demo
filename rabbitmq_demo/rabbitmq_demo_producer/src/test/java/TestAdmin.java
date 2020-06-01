import com.producer.ProducerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by fyw on 2020/5/31.
 */
@SpringBootTest(classes = ProducerApplication.class)
@RunWith(SpringRunner.class)
public class TestAdmin {
    @Autowired
    RabbitAdmin rabbitAdmin;



    @Test
    public void testAdmin() throws Exception{
        rabbitAdmin.declareExchange(new DirectExchange("spring_direct",true,false));

        rabbitAdmin.declareExchange(new TopicExchange("spring_topic",true,false));

        rabbitAdmin.declareExchange(new FanoutExchange("spring_fanout",true,false));


        rabbitAdmin.declareQueue(new Queue("spring_direct_queue",true,false,false));
        rabbitAdmin.declareQueue(new Queue("spring_topic_queue",true,false,false));

        /**
         * Binding(String destination, Binding.DestinationType destinationType,
         * String exchange, String routingKey, Map<String, Object> arguments)
         *
         * destination 队列名称
         * destinationType 绑定类型（队列、交换机）
         * exchange 绑定的交换机
         * routingKey 绑定的rotingKey
         * arguments 扩展参数
         */
        rabbitAdmin.declareBinding(new Binding("spring_direct_queue",Binding.DestinationType.QUEUE,
                "spring_direct","direct_sp",null));

        rabbitAdmin.declareBinding(new Binding("spring_topic_queue",Binding.DestinationType.QUEUE,
                "spring_topic","topic.#",null));


    }

}
