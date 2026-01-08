package com.nextepisode.user_service.events.movie;

import com.nextepisode.user_service.config.RabbitMQConfig;
import com.nextepisode.user_service.entity.movie.Movie;
import com.nextepisode.user_service.entity.user.User;
import com.nextepisode.user_service.repo.GenreRepository;
import com.nextepisode.user_service.repo.UserMovieRepository;
import com.nextepisode.user_service.service.MovieService;
import com.nextepisode.user_service.service.UserMovieService;
import com.nextepisode.user_service.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@AllArgsConstructor
public class MovieEnrichedStatusListener {

    private final UserMovieService userMovieService;
    private final UserService userService;
    private final MovieService movieService;

    @RabbitListener(queues = RabbitMQConfig.MOVIE_ENRICHED_QUEUE)
    public void handleMovieEnriched(MovieEnrichedStatusEvent event) {
        log.info("Received enriched movie data: {}", event);

        try {

            if (userMovieService.existsByUserUsernameAndMovieId(event.getUserId(), event.getMovieId())) {
                throw new RuntimeException("Movie already in favorites");
            }

            // Create movie from the event
            Movie movie = movieService.createFromEnrichedEvent(event);

            // Create user by username attached tot
            User user = userService.getUserByUsername(event.getUserId());

            // create user movie record
            userMovieService.createFromMovieEnrichedEvent(movie, user, event);

            log.info("Favorite enriched successfully for userId={}, movieId={}", event.getUserId(), event.getMovieId());

        } catch (Exception e) {
            log.error("Error enriching favorite", e);
        }
    }
}
