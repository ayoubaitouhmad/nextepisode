package com.nextepisode.user_service.events.movie;

import com.nextepisode.user_service.config.RabbitMQConfig;
import com.nextepisode.user_service.dto.request.MovieStatusRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMovieEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishUserMovieEvent(String userId, MovieStatusRequest movieStatusRequest) {
        log.info("Publishing favorite added event: userId={}, movieId={}", userId, movieStatusRequest.getMovieId());
        UserMovieStatusEvent event = new UserMovieStatusEvent(userId, movieStatusRequest.getMovieId(), movieStatusRequest.getCategory().name(), movieStatusRequest.getAction().name());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_MOVIE_STATUS_EXCHANGE,
                RabbitMQConfig.USER_MOVIE_STATUS_ROUTING_KEY,
                event
        );
        log.debug("Publishing favorite added event: userId={}, movieId={}", userId, movieStatusRequest.getMovieId());
    }

}
