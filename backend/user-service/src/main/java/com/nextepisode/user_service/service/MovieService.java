package com.nextepisode.user_service.service;

import com.nextepisode.user_service.entity.movie.Movie;
import com.nextepisode.user_service.entity.movie.MovieGenre;
import com.nextepisode.user_service.events.movie.MovieEnrichedStatusEvent;
import com.nextepisode.user_service.exception.ErrorCode;
import com.nextepisode.user_service.exception.ResourceNotFoundException;
import com.nextepisode.user_service.repo.GenreRepository;
import com.nextepisode.user_service.repo.MovieRepository;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    /**
     * Find user by username
     */
    @Transactional(readOnly = true)
    public Optional<Movie> findById(Long id) {
        log.debug("Finding movie by username: {}", id);
        return movieRepository.findById(id);
    }

    /**
     * Get user by username (throws exception if not found)
     */
    @Transactional(readOnly = true)
    public Movie getMovieBId(Long id) {
        log.debug("Getting user by username: {}", id);
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "movie", id));
    }

    @Transactional
    public Movie create(Movie movie) {
        log.debug("Start saving movie:{}", movie);

        try {
            Movie savedMovie = movieRepository.save(movie);
            log.debug("Movie with id:{} saved successfully ", movie.getId());
            return savedMovie;
        } catch (PersistenceException e) {
            log.error("Failed to create movie:{}", movie, e);
            throw e;
        }
    }

    @Transactional
    public Movie createFromEnrichedEvent(MovieEnrichedStatusEvent event) {
        log.debug("Create movie record from enriched event [RabbitMq], event:{}", event);

        Movie movie = new Movie();
        movie.setId(event.getId());
        movie.setTitle(event.getTitle());
        movie.setPosterPath(event.getPosterPath());
        movie.setAdult(event.getAdult());
        movie.setOriginalTitle(event.getOriginalTitle());
        movie.setOverview(event.getOverview());
        movie.setOriginalLanguage(event.getOriginalLanguage());
        movie.setBackdropPath(event.getBackdropPath());
        movie.setReleaseDate(LocalDate.parse(event.getReleaseDate()));
        movie.setHomepage(event.getHomepage());
        movie.setPopularity(event.getPopularity());
        movie.setStatus(event.getStatus());
        movie.setVoteCount(event.getVoteCount());
        if (!event.getGenres().isEmpty()) {
            List<MovieGenre> movieGenres = genreRepository.findByNamesIgnoreCase(event.getGenres());
            movie.setGenres(movieGenres);
        }
        movie = create(movie);

        log.debug("Movie record created successfully from enriched event [RabbitMq], event:{}, movie:{}", event, movie);
        return movie;
    }

}
