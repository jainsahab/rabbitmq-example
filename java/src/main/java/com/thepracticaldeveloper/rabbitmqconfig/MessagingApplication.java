package com.thepracticaldeveloper.rabbitmqconfig;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class MessagingApplication {

    public static final String EXCHANGE_NAME = "java-exchange";
    public static final String QUEUE_NAME = "java-queue";
    public static final String ROUTING_KEY = "messages.key";

    public static void main(String[] args) {
        SpringApplication.run(MessagingApplication.class, args);
    }

    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue appQueue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding declareBindingSpecific() {
        return BindingBuilder.bind(appQueue()).to(appExchange()).with(ROUTING_KEY);
    }

    //-------------------------------------
    @Bean
    public TopicExchange appRetryExchange() {
        return new TopicExchange(CustomMessageListenerRetry.EXCHANGE_NAME);
    }

    @Bean
    public AbstractExchange appDelayedMessageExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new DelayedMessageExchange(CustomMessageListenerRetry.DELAYED_EXCHANGE_NAME, true, false, args);
    }

    @Bean
    public Queue appRetryQueue() {
        return new Queue(CustomMessageListenerRetry.QUEUE_NAME);
    }

    @Bean
    public Binding declareRetryBindingSpecific() {
        return BindingBuilder.bind(appRetryQueue()).to(appRetryExchange()).with(CustomMessageListenerRetry.ROUTING_KEY);
    }

    @Bean
    public Binding declareDelayedMessageBinding() {
        return BindingBuilder.bind(appRetryQueue()).to(appDelayedMessageExchange()).with(CustomMessageListenerRetry.QUEUE_NAME).noargs();
    }
//-------------------------------------

    // You can comment the two methods below to use the default serialization / deserialization (instead of JSON)
//    @Bean
//    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
//        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
//        return rabbitTemplate;
//    }

//    @Bean
//    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

}
