package com.thepracticaldeveloper.rabbitmqconfig;

import org.springframework.amqp.core.AbstractExchange;

import java.util.Map;

public class DelayedMessageExchange extends AbstractExchange {
    public DelayedMessageExchange(String name) {
        super(name);
    }

    public DelayedMessageExchange(String name, boolean durable, boolean autoDelete) {
        super(name, durable, autoDelete);
    }

    public DelayedMessageExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments) {
        super(name, durable, autoDelete, arguments);
    }

    @Override
    public String getType() {
        return "x-delayed-message";
    }
}
