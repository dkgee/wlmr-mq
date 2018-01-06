package com.dk.rabbitmq.workqueues;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 * Author: jht
 * Date: 2018/1/6
 */
public class Worker {

    private final static String TASK_QUEUE_NAME="hello";

    public static void main(String[] args) {
        try {
            test();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void test() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println(" [x] Received '" + message +"'");
                try {
                    doWork(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println(" [x] Done");
                }
            }
        };

        boolean autoAck =  true;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);

    }

    private static void doWork(String task) throws InterruptedException {
        for(char ch: task.toCharArray()){
            if(ch == '.'){
                Thread.sleep(1000);
            }

        }
    }

}
