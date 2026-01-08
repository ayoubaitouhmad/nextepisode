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

    // ════════════════════════════════════════════════════════════
    // USER REGISTRATION EVENTS
    // ════════════════════════════════════════════════════════════
    public static final String USER_EXCHANGE = "user.exchange";
    public static final String USER_REGISTERED_QUEUE = "user.registered.queue";
    public static final String USER_REGISTERED_ROUTING_KEY = "user.registered";
    public static final String USER_DLX_EXCHANGE = "user.dlx";
    public static final String USER_DLQ = "user.dlq";

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

    // ════════════════════════════════════════════════════════════
    // USER MOVIE STATUS EVENTS
    // ════════════════════════════════════════════════════════════

    // Queue names
    public static final String USER_MOVIE_STATUS_QUEUE = "user.movie.status.queue";
    public static final String MOVIE_ENRICHED_QUEUE = "movie.enriched.queue";

    // Exchange names
    public static final String USER_MOVIE_STATUS_EXCHANGE = "user.movie.status.exchange";
    public static final String MOVIE_ENRICHED_EXCHANGE = "movie.enriched.exchange";

    // Routing keys
    public static final String USER_MOVIE_STATUS_ROUTING_KEY = "user.movie.status.changed";
    public static final String MOVIE_ENRICHED_ROUTING_KEY = "movie.enriched";

    // Dead Letter Configuration for Movie Status
    public static final String MOVIE_STATUS_DLX_EXCHANGE = "user.movie.status.dlx";
    public static final String MOVIE_STATUS_DLQ = "user.movie.status.dlq";

    // Queues
    @Bean
    public Queue userMovieStatusQueue() {
        return QueueBuilder.durable(USER_MOVIE_STATUS_QUEUE)
                .withArgument("x-dead-letter-exchange", MOVIE_STATUS_DLX_EXCHANGE)
                .build();
    }

    @Bean
    public Queue movieEnrichedQueue() {
        return QueueBuilder.durable(MOVIE_ENRICHED_QUEUE)
                .withArgument("x-dead-letter-exchange", MOVIE_STATUS_DLX_EXCHANGE)
                .build();
    }

    @Bean
    public Queue movieStatusDlq() {
        return QueueBuilder.durable(MOVIE_STATUS_DLQ).build();
    }

    // Exchanges
    @Bean
    public TopicExchange userMovieStatusExchange() {
        return new TopicExchange(USER_MOVIE_STATUS_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange movieEnrichedExchange() {
        return new TopicExchange(MOVIE_ENRICHED_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange movieStatusDlxExchange() {
        return new DirectExchange(MOVIE_STATUS_DLX_EXCHANGE, true, false);
    }

    // Bindings
    @Bean
    public Binding userMovieStatusBinding() {
        return BindingBuilder
                .bind(userMovieStatusQueue())
                .to(userMovieStatusExchange())
                .with(USER_MOVIE_STATUS_ROUTING_KEY);
    }

    @Bean
    public Binding movieEnrichedBinding() {
        return BindingBuilder
                .bind(movieEnrichedQueue())
                .to(movieEnrichedExchange())
                .with(MOVIE_ENRICHED_ROUTING_KEY);
    }

    @Bean
    public Binding movieStatusDlqBinding() {
        return BindingBuilder
                .bind(movieStatusDlq())
                .to(movieStatusDlxExchange())
                .with("#");
    }

    // ════════════════════════════════════════════════════════════
    // MESSAGE CONVERTER & LISTENER FACTORY
    // ════════════════════════════════════════════════════════════

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

        // Optional: Configure retry and error handling
        factory.setDefaultRequeueRejected(false); // Send to DLQ on failure
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);

        return factory;
    }
}