package com.dk.rabbitmq.workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by jht on 2018/1/6.
 */
public class NewTask {

    private final static String TASK_QUEUE_NAME="hello";

    public static void main(String[] args) {
        try {
            test(args);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void test(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        //是否持久保存队列
        boolean durable=true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);

        String message = getMessage(args);
        //设置队列消息持久化 MessageProperties.PERSISTENT_TEXT_PLAIN
        channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

        //根据工作节点任务量实际大小分发任务
        int prefetchcount = 1;
        channel.basicQos(prefetchcount);
        System.out.println(" [x] Sent '" + message + "'");
    }

    private static String getMessage(String[] strings){
        if(strings.length < 1){
            return "Hello World!";
        }
        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter){
        int length = strings.length;
        if(length == 0) {
            return "";
        }
        StringBuilder words = new StringBuilder(strings[0]);
        for(int i = 1; i < length; i++){
            words.append(delimiter).append(strings[i]);
        }

        return words.toString();
    }
}
