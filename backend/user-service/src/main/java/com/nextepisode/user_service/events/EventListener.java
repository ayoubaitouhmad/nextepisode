package com.nextepisode.user_service.events;

import com.nextepisode.user_service.config.RabbitMQConfig;
import com.nextepisode.user_service.entity.User;
import com.nextepisode.user_service.repo.UserRepository;
import com.nextepisode.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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
    private final UserRepository userRepository;

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
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("✅ Received user registered event from RabbitMQ");
        log.info("   eventId: {}", event.getEventId());
        log.info("   userId: {}", event.getUserId());
        log.info("   username: {}", event.getUsername());
        log.info("   email: {}", event.getEmail());
        log.info("   sourceService: {}", event.getSourceService());
        log.info("═══════════════════════════════════════════════════════════════");


        String username = event.getUsername();
        String email = event.getEmail();
        LocalDateTime createdAt = event.getCreatedAt();

        Optional<User> existingUser = userService.findByUsername(username);
        if (existingUser.isEmpty()) {
            log.info("═══════════════════════════════════════════════════════════════");
            log.info("Creating new user");
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setCreatedAt(createdAt);
            userRepository.save(user);
        }
    }
}