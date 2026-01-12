package com.nextepisode.user_service.service;

import com.nextepisode.user_service.dto.*;
import com.nextepisode.user_service.dto.request.MovieStatusRequest;
import com.nextepisode.user_service.entity.movie.Movie;
import com.nextepisode.user_service.entity.user.User;
import com.nextepisode.user_service.entity.user.UserMovie;
import com.nextepisode.user_service.events.movie.MovieEnrichedStatusEvent;
import com.nextepisode.user_service.events.movie.UserMovieEventPublisher;
import com.nextepisode.user_service.exception.codes.BusinessValidationCodes;
import com.nextepisode.user_service.exception.exceptions.BusinessValidationException;
import com.nextepisode.user_service.repo.UserMovieRepository;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMovieService {

    private final UserMovieRepository userMovieRepository;

    private final UserService userService;
    private final MovieService movieService;
    private final UserMovieEventPublisher userMovieEventPublisher;

    /***
     * Create new user movie record from an instance
     * @param userMovie
     * @return
     */
    @Transactional
    public UserMovie create(UserMovie userMovie) {
        log.debug("Start saving user movie record record:{}", userMovie);

        // chek
        if (!userMovie.hasMovie()) {
            throw new BusinessValidationException(BusinessValidationCodes.MOVIE_REQUIRED);
        }

        if (!userMovie.hasUser()) {
            throw new BusinessValidationException(BusinessValidationCodes.USER_REQUIRED);
        }

        try {
            UserMovie savedUserMovie = userMovieRepository.save(userMovie);
            log.debug("Movie with id:{} saved successfully ", userMovie.getId());
            return savedUserMovie;
        } catch (PersistenceException e) {
            log.error("Failed to create movie:{}", userMovie, e);
            throw e;
        }
    }

    @Transactional
    public UserMovie createFromMovieEnrichedEvent(Movie movie, User user, MovieEnrichedStatusEvent event) {
        log.debug("Start creating user movie record from movie:{}, user:{}, event:{}", movie, user, event);

        try {
            UserMovie userMovie = new UserMovie();
            userMovie.setMovie(movie);
            userMovie.setUser(user);
            changeMovieStatusFromEvent(userMovie, event);
            userMovie = create(userMovie);

            log.debug("Successfully movie record created from record:{}, movie:{}, user:{}", userMovie, movie, user);
            return userMovie;

        } catch (PersistenceException e) {
            log.debug("Failed to create movie record from movie:{}, user:{}, event:{}", movie, user, event, e);
            throw e;
        }
    }

    /**
     * Change user movie record status based on the MovieEnrichedEvent
     *
     * @param userMovie
     * @param event
     */
    private void changeMovieStatusFromEvent(UserMovie userMovie, MovieEnrichedStatusEvent event) {
        log.info("Start getting action from event, action:{}", event.getAction());
        MovieStatusRequest.Action action = MovieStatusRequest.Action.fromName(event.getAction());
        if (action != null) {
            log.debug("Get action successfully action:{}", action);
        } else {
            log.debug("No action found for action:{}", action);
        }

        log.info("Start getting category from event, category:{}", event.getCategory());
        MovieStatusRequest.Category category = MovieStatusRequest.Category.fromName(event.getCategory());
        if (action != null) {
            log.debug("Get category successfully category:{}", category);
        } else {
            log.debug("No category found for category:{}", category);
        }

        if (action == null && category == null) {
            return;
        }
        changeMovieStatus(userMovie, action, category);
    }


    public void changeMovieStatus(UserMovie userMovie, MovieStatusRequest.Action action, MovieStatusRequest.Category category) {
        boolean isAdding = action == MovieStatusRequest.Action.ADD;
        switch (category) {
            case FAVORITE:
                userMovie.setFavorite(isAdding);
                break;
            case WATCHED:
                userMovie.setWatched(isAdding);
                break;
            case WATCHLIST:
                userMovie.setInWatchlist(isAdding);
                break;
        }
    }

    @Transactional(readOnly = true)
    public Boolean existsByUserUsernameAndMovieId(String username, Long movieId) {
        log.debug("Check if a record exist by username:{} and movie;=:{} ", username, movieId);
        return userMovieRepository.existsByUserUsernameAndMovieId(username, movieId);
    }

    @Transactional(readOnly = true)
    public MovieListResponse getUserFavoriteMovies(String username, Pageable pageable) {
        log.debug("Finding user:{} favorite movies, page:{}", username, pageable.getPageNumber());
        Page<MovieResponse> page = userMovieRepository.findUserFavoriteMovies(username, pageable);
        return buildMovieListResponse(page);
    }

    @Transactional(readOnly = true)
    public MovieListResponse getUserWatchedMovies(String username, Pageable pageable) {
        log.debug("Finding user:{} watched movies, page:{}", username, pageable.getPageNumber());
        Page<MovieResponse> page = userMovieRepository.findUserWatchedMovies(username, pageable);
        return buildMovieListResponse(page);
    }

    @Transactional(readOnly = true)
    public MovieListResponse getUserWatchlistMovies(String username, Pageable pageable) {
        log.debug("Finding user:{} watchlist movies, page:{}", username, pageable.getPageNumber());
        Page<MovieResponse> page = userMovieRepository.findUserWatchlistMovies(username, pageable);
        return buildMovieListResponse(page);
    }


    @Transactional(readOnly = true)
    public UserMovieTvStats getUserMoviesStats(String username) {
        return userMovieRepository.getUserMovieStats(username);
    }

    private MovieListResponse buildMovieListResponse(Page<MovieResponse> page) {
        return MovieListResponse.builder().page(page.getNumber()).results(page.getContent()).totalPages((long) page.getTotalPages()).totalResults(page.getTotalElements()).build();
    }

    @Transactional(readOnly = true)
    public MovieStatus getUserMoviesStats(Long movieId, String username) {
        log.debug("Finding user movie by movieId:{} and username:{} ", movieId, username);
        return userMovieRepository.findMovieStatus(movieId, username).orElse(MovieStatus.defaultStatus());
    }

    @Transactional
    public MovieStatus changeMovieStatus(MovieStatusRequest movieStatusRequest, String username) {
        log.debug("Updating movie status for movie:{}, category:{},  action:{} , user:{}", movieStatusRequest.getMovieId(), movieStatusRequest.getCategory(), movieStatusRequest.getAction(), username);

        Optional<UserMovie> userMovie = userMovieRepository
                .findByMovieIdAndUserUsername(movieStatusRequest.getMovieId(), username);
        if (userMovie.isPresent()) {
            boolean isAdding = movieStatusRequest.getAction() == MovieStatusRequest.Action.ADD;
            switch (movieStatusRequest.getCategory()) {
                case FAVORITE:
                    userMovie.get().setFavorite(isAdding);
                    break;
                case WATCHED:
                    userMovie.get().setWatched(isAdding);
                    break;
                case WATCHLIST:
                    userMovie.get().setInWatchlist(isAdding);
                    break;
            }

            if (!userMovie.get().isFavorite() && !userMovie.get().isWatched() && !userMovie.get().isInWatchlist()) {
                userMovieRepository.delete(userMovie.get());
                return MovieStatus.defaultStatus();
            }
            UserMovie saved = userMovieRepository.save(userMovie.get());
            return new MovieStatus(saved.isFavorite(), saved.isWatched(), saved.isInWatchlist());
        }
        return createNewUserMovie(movieStatusRequest, username);

    }

    /**
     *
     * @param movieStatusRequest
     * @param username
     * @return
     */
    @Transactional
    protected MovieStatus createNewUserMovie(MovieStatusRequest movieStatusRequest, String username) {
        log.debug("Start creating new user movie record for user:{}, movie:{} , action:{}, category:{}", username, movieStatusRequest.getMovieId(), movieStatusRequest.getAction(), movieStatusRequest.getCategory());

        User user = userService.getUserByUsername(username);
        Optional<Movie> movie = movieService.findById(movieStatusRequest.getMovieId());
        if (movie.isPresent()) {
            UserMovie userMovie = new UserMovie();
            userMovie.setMovie(movie.get());
            userMovie.setUser(user);
            changeMovieStatus(userMovie, movieStatusRequest.getAction(), movieStatusRequest.getCategory());

            if (!userMovie.isFavorite() && !userMovie.isWatched() && !userMovie.isInWatchlist()) {
                userMovieRepository.delete(userMovie);
                return MovieStatus.defaultStatus();
            }

            userMovie = create(userMovie);
            log.debug("user movie created successfully record for user:{}, movie:{} ", username, movieStatusRequest.getMovieId());
            return new MovieStatus(userMovie.isFavorite(), userMovie.isWatched(), userMovie.isInWatchlist());
        }

        log.debug("Movie with id:{} not exists, publish publishFavoriteAdded", movieStatusRequest.getMovieId());
        userMovieEventPublisher.publishUserMovieEvent(username, movieStatusRequest);
        return buildOptimisticStatus(movieStatusRequest);

    }

    private MovieStatus buildOptimisticStatus(MovieStatusRequest request) {
        boolean isAdding = request.getAction() == MovieStatusRequest.Action.ADD;
        return switch (request.getCategory()) {
            case FAVORITE -> new MovieStatus(isAdding, false, false);
            case WATCHED -> new MovieStatus(false, isAdding, false);
            case WATCHLIST -> new MovieStatus(false, false, isAdding);
        };
    }


    public MovieStatusList findByUsernameAndMovieIds(String username, List<Integer> movieIds) {
        log.debug("Start getting movies status for user:{}, movies:{}", username, movieIds);

        try {
            List<UserMovie> userMovies = userMovieRepository
                    .findByUserUsernameAndMovieIdIn(username, movieIds);

            Map<Long, MovieStatus> statusMap = new HashMap<>();
            userMovies.forEach(userMovie -> {
                statusMap.put(userMovie.getMovie().getId(), new MovieStatus(
                        userMovie.isFavorite(), userMovie.isWatched(), userMovie.isInWatchlist()
                ));
            });
            log.debug("Getting movies status successfully for user:{}, movies:{}", username, movieIds);

            return new MovieStatusList(statusMap);

        } catch (PersistenceException e) {
            log.error("Failed to get movies status for user:{}, movies:{}", username, movieIds);
            throw e;
        }


    }


}