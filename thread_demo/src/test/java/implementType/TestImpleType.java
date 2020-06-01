package implementType;

import com.thread.ThreadApplication;
import com.thread.implementType.MyCallAble;
import com.thread.implementType.MyExecutor;
import com.thread.implementType.MyRunable;
import com.thread.implementType.MyThread;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.FutureTask;

/**
 * Created by fyw on 2020/6/1.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThreadApplication.class)
public class TestImpleType {

    @Autowired
    MyThread myThread;
    /**
     * 继承Thread的测试(没有返回值)
     */
    @Test
    public void testThread(){
        myThread.start();

        /**
         * 打印结果
         * myThread执行了_____0
         * myThread执行了_____1
         * myThread执行了_____2
         * myThread执行了_____3
         * myThread执行了_____4
         */
    }


    /**
     * 实现Runable接口（没有返回值）
     */
    @Test
    public void testRunable(){
        MyRunable myRunable = new MyRunable();
        //为了启动线程，创建一个thread类传入自己的myrunable
        Thread thread = new Thread(myRunable);
        thread.start();
        thread.destroy();

        /**
         * 打印结果
         * MyRunable线程执行了_____0
         * MyRunable线程执行了_____1
         * MyRunable线程执行了_____2
         * MyRunable线程执行了_____3
         * MyRunable线程执行了_____4
         */
    }

    /**
     * 实现callable接口，有返回值
     */
    @Test
    public void testCallable() throws Exception{
        MyCallAble myCallAble = new MyCallAble();
        //futureTask接收myCallable来创建，futureTask同时实现了Future和Runable
        FutureTask<String> futureTask = new FutureTask<String>(myCallAble);
        //线程的启动
        Thread thread = new Thread(futureTask);
        thread.start();
        System.out.println(futureTask.get());

        /**
         * 打印结果
         * MyCallAble线程执行了
         */

    }

    /**
     * 测试Executor线程池（可以有返回值callable submit，也可以没有 execute）
     * 切记：局部创建的线程池，使用之后一定要关闭，一定，不然很可能造成线程泄露甚至内存溢出
     */
    @Autowired
    MyExecutor myExecutor;

    @Test
    public void testExecutor() throws Exception{
        myExecutor.call();
        /**
         * 打印结果
         * 1
         * 2
         * 3
         * 线程池已经执行完毕了
         */
        System.out.println(myExecutor.callV());
        /**
         * 打印结果
         * 1---2----123_---2
         */

    }
}
