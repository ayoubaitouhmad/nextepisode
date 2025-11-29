package com.nextepisode.auth_service.events;



import com.nextepisode.auth_service.config.RabbitMQConfig;
import com.nextepisode.auth_service.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Publishes events to RabbitMQ.
 * Used by Auth Service to notify other services.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;


    public void publishUserRegisteredEvent(User user) {
        try {
            UserRegisteredEvent event = UserRegisteredEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .createdAt(LocalDateTime.now())
                    .sourceService("auth-service")
                    .version("1.0")
                    .build();

            log.info("Publishing user registered event: userId={}, email={}", user.getId(), user.getEmail());

            // Send to RabbitMQ
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USER_EXCHANGE,
                    RabbitMQConfig.USER_REGISTERED_ROUTING_KEY,
                    event
            );

            log.info("Event published successfully: eventId={}", event.getEventId());

        } catch (Exception e) {
            log.error("Failed to publish user registered event: userId={}, error={}", user.getId(), e.getMessage(), e);
            // In production, implement retry logic or dead letter queue
            throw new RuntimeException("Failed to publish event", e);
        }
    }
}