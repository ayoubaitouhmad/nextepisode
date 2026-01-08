package com.nextepisode.tmdb_service.events.movie;

import com.nextepisode.tmdb_service.config.RabbitMQConfig;
import com.nextepisode.tmdb_service.service.MovieService;
import com.nextepisode.tmdb_service.tmdb.response.MovieDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MovieEnrichedStatusListener {

    @Autowired
    private MovieService movieRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.USER_MOVIE_STATUS_QUEUE)
    public void handleFavoriteAdded(UserMovieStatusEvent event) {
        log.info("Received favorite event: {}", event);

        try {
            // Fetch movie data from database
            MovieDetails movie = movieRepository.getMovieById(event.getMovieId());

            // Create enriched event
            MovieEnrichedStatusEvent enriched = new MovieEnrichedStatusEvent(
                    // Event data
                    event.getUserId(),
                    event.getMovieId(),
                    event.getCategory(),
                    event.getAction(),
                    // tmdb data
                    movie.getId(),
                    movie.isAdult(),
                    movie.getTitle(),
                    movie.getOriginalTitle(),
                    movie.getOverview(),
                    movie.getOriginalLanguage(),
                    movie.getPosterPath(),
                    movie.getBackdropPath(),
                    movie.getReleaseDate(),
                    movie.getHomepage(),
                    movie.getPopularity(),
                    movie.getStatus(),
                    movie.getVoteCount(),
                    movie.getLowercaseGenreNames()
            );

            // Publish enriched data back to user-service
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.MOVIE_ENRICHED_EXCHANGE,
                    RabbitMQConfig.MOVIE_ENRICHED_ROUTING_KEY,
                    enriched
            );

            log.info("Published enriched movie data for movieId={}", movie.getId());

        } catch (Exception e) {
            log.error("Error enriching movie data", e);
        }
    }

}
