package com.dk.rabbitmq.workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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

        String message = getMessage(args);
        channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes());
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
