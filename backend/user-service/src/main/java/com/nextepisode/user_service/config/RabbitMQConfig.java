package com.nextepisode.user_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String USER_EXCHANGE = "user.exchange";
    public static final String USER_REGISTERED_QUEUE = "user.registered.queue";
    public static final String USER_REGISTERED_ROUTING_KEY = "user.registered";
    public static final String USER_DLX_EXCHANGE = "user.dlx";
    public static final String USER_DLQ = "user.dlq";

    // ════════════════════════════════════════════════════════
    // IMPORTANT: EXACT SAME CONFIGURATION AS auth-service
    // ════════════════════════════════════════════════════════

    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange(USER_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange userDlxExchange() {
        return new DirectExchange(USER_DLX_EXCHANGE, true, false);
    }

    @Bean
    public Queue userRegisteredQueue() {
        return QueueBuilder.durable(USER_REGISTERED_QUEUE)
                .withArgument("x-dead-letter-exchange", USER_DLX_EXCHANGE)
                .build();
    }

    @Bean
    public Queue userDlq() {
        return QueueBuilder.durable(USER_DLQ)
                .build();
    }

    @Bean
    public Binding userRegisteredBinding(
            Queue userRegisteredQueue,
            DirectExchange userExchange) {
        return BindingBuilder.bind(userRegisteredQueue)
                .to(userExchange)
                .with(USER_REGISTERED_ROUTING_KEY);
    }

    @Bean
    public Binding userDlqBinding(
            Queue userDlq,
            DirectExchange userDlxExchange) {
        return BindingBuilder.bind(userDlq)
                .to(userDlxExchange)
                .with("#");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }

}