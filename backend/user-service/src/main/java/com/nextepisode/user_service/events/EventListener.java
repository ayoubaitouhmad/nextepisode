package com.nextepisode.user_service.events;

import com.nextepisode.user_service.config.RabbitMQConfig;
import com.nextepisode.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Listens to events from Auth Service via RabbitMQ.
 * Creates user profile in User Service when registration event received.
 * <p>
 * ✅ CORRECTED: Added full event handling implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventListener {

    private final UserService userService;

    /**
     * Consume user registration event from queue.
     * Called automatically when message arrives in user.registered.queue
     *
     * @param event UserRegisteredEvent published by Auth Service
     *              <p>
     *              IMPORTANT: Do NOT re-throw exceptions!
     *              If exception is thrown, RabbitMQ will requeue the message
     *              and the listener will try again infinitely, causing repeated errors.
     *              Instead, catch exceptions, log them, and let the message be acknowledged.
     */
    @RabbitListener(
            queues = RabbitMQConfig.USER_REGISTERED_QUEUE,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void handleUserRegisteredEvent(UserRegisteredEvent event) throws Exception {
        log.debug("═══════════════════════════════════════════════════════════════");
        log.debug("✅ Received user registered event from RabbitMQ");
        log.debug("   eventId: {}", event.getEventId());
        log.debug("   userId: {}", event.getUserId());
        log.debug("   username: {}", event.getUsername());
        log.debug("   email: {}", event.getEmail());
        log.debug("   sourceService: {}", event.getSourceService());
        log.debug("═══════════════════════════════════════════════════════════════");
        userService.createUserFromRegisteredEvent(event.getUsername() , event.getEmail());
    }
}