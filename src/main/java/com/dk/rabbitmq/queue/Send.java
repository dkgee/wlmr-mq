package com.dk.rabbitmq.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 描述：
 * 作者：JinHuaTao
 * 时间：2018/1/5 18:20
 */
public class Send {
    private final static String QUEUE_NAME="hello";

    public static void main(String[] args) {
        try {
            test();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向消息队列中发送消息
     *      step1: 使用工厂方法创建连接
     *      step2: 使用连接创建频道
     *      step3: 使用频道创建队列
     *      step3：向队列发送消息
     *
     *
     * */
    public static void test() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //创建发送队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        for(int i=1; i <= 10; i++){
            String message = "I am number:" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }

        channel.close();
        connection.close();
    }
}
