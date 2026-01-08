package com.nextepisode.tmdb_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ════════════════════════════════════════════════════════════
    // USER MOVIE STATUS EVENTS
    // IMPORTANT: Must match user-service configuration exactly
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

    // Dead Letter Configuration
    public static final String MOVIE_STATUS_DLX_EXCHANGE = "user.movie.status.dlx";
    public static final String MOVIE_STATUS_DLQ = "user.movie.status.dlq";

    // ════════════════════════════════════════════════════════════
    // QUEUES
    // ════════════════════════════════════════════════════════════

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

    // ════════════════════════════════════════════════════════════
    // EXCHANGES
    // ════════════════════════════════════════════════════════════

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

    // ════════════════════════════════════════════════════════════
    // BINDINGS
    // ════════════════════════════════════════════════════════════

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
    // MESSAGE CONVERTER & RABBIT TEMPLATE
    // ════════════════════════════════════════════════════════════

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}