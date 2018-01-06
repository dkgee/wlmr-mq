package com.dk.rabbitmq.queue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 描述：
 * 作者：JinHuaTao
 * 时间：2018/1/5 18:35
 */
public class Recv {

    private final static String QUEUE_NAME="hello";

    public static void main(String[] args) {
        try {
            recv();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从消息队列接收消息
     *      step01: 创建连接工厂
     *      step02: 使用连接工厂创建频道
     *      step03: 使用频道验证队列是否创建
     *      step04: 创建消费端侦听频道
     *      step05: 启动消费服务
     *
     * */
    public static void recv() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("[*] Waiting for messsages. To exit press CTRL+C");
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
