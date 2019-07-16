package com.thepracticaldeveloper.rabbitmqconfig;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomMessageListenerRetry {

    private static final Logger log = LoggerFactory.getLogger(CustomMessageListenerRetry.class);
    public static final String QUEUE_NAME = "retry-feature-queue";
    public static final String EXCHANGE_NAME = "retry-feature-exchange";
    public static final String DELAYED_EXCHANGE_NAME = "delayed-retry-exchange";
    public static final String ROUTING_KEY = "messages.key";


    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMessage(final Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("Received message on retry listener: {}", new String(message.getBody()));
        channel.basicAck(tag, false);


        Object retryCountHeader = message.getMessageProperties().getHeaders().get("x-retries");
        int retryCount = retryCountHeader == null ? 1 : (Integer) retryCountHeader;
        if (retryCount > 10) {
            System.out.println("Threshold crossed, no more retrying");
            return;
        }
        System.out.println("Retrying " + retryCount + " times.");
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-delay", 2000);
        headers.put("x-retries", ++retryCount);
        AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder().headers(headers);
        channel.basicPublish(DELAYED_EXCHANGE_NAME, QUEUE_NAME, props.build(), message.getBody());
    }
}
