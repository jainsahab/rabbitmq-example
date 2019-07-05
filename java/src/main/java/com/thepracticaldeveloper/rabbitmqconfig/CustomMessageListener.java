package com.thepracticaldeveloper.rabbitmqconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CustomMessageListener {

    private static final Logger log = LoggerFactory.getLogger(CustomMessageListener.class);

    @RabbitListener(queues = MessagingApplication.QUEUE_NAME)
    public void receiveMessage(final CustomMessage customMessage) {
        log.info("Received message as specific class: {}", customMessage.toString());
    }
}
