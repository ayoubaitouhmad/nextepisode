package com.nextepisode.user_service.service;

import com.nextepisode.user_service.dto.MovieStatus;
import com.nextepisode.user_service.dto.request.MovieStatusRequest;
import com.nextepisode.user_service.entity.movie.Movie;
import com.nextepisode.user_service.entity.user.User;
import com.nextepisode.user_service.entity.user.UserMovie;
import com.nextepisode.user_service.events.movie.UserMovieEventPublisher;
import com.nextepisode.user_service.exception.exceptions.BusinessValidationException;
import com.nextepisode.user_service.repo.UserMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserMovieService Unit Tests")
class UserMovieServiceTest {

    @Mock
    private UserMovieRepository userMovieRepository;

    @Mock
    private UserService userService;

    @Mock
    private MovieService movieService;

    @Mock
    private UserMovieEventPublisher userMovieEventPublisher;

    @InjectMocks
    private UserMovieService userMovieService;

    private User testUser;
    private Movie testMovie;
    private UserMovie testUserMovie;

    private static final String TEST_USERNAME = "testuser";
    private static final Long TEST_MOVIE_ID = 12345L;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername(TEST_USERNAME);

        testMovie = new Movie();
        testMovie.setId(TEST_MOVIE_ID);
        testMovie.setTitle("Test Movie");

        testUserMovie = new UserMovie();
        testUserMovie.setUser(testUser);
        testUserMovie.setMovie(testMovie);
        testUserMovie.setFavorite(false);
        testUserMovie.setWatched(false);
        testUserMovie.setInWatchlist(false);
    }

    // ==================== CREATE TESTS ====================

    @Nested
    @DisplayName("create() method")
    class CreateTests  {

        @Test
        @DisplayName("Should create user movie record successfully")
        void shouldCreateUserMovieSuccessfully() {
            // Arrange
            when(userMovieRepository.save(any(UserMovie.class)))
                    .thenReturn(testUserMovie);

            // Act
            UserMovie result = userMovieService.create(testUserMovie);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getUser()).isEqualTo(testUser);
            assertThat(result.getMovie()).isEqualTo(testMovie);
            verify(userMovieRepository).save(testUserMovie);
        }

        @Test
        @DisplayName("Should throw BusinessValidationException when movie is missing")
        void shouldThrowExceptionWhenMovieMissing() {
            // Arrange
            UserMovie invalidUserMovie = new UserMovie();
            invalidUserMovie.setUser(testUser);
            // No movie set

            // Act & Assert
            assertThatThrownBy(() -> userMovieService.create(invalidUserMovie))
                    .isInstanceOf(BusinessValidationException.class);

            verify(userMovieRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw BusinessValidationException when user is missing")
        void shouldThrowExceptionWhenUserMissing() {
            // Arrange
            UserMovie invalidUserMovie = new UserMovie();
            invalidUserMovie.setMovie(testMovie);
            // No user set

            // Act & Assert
            assertThatThrownBy(() -> userMovieService.create(invalidUserMovie))
                    .isInstanceOf(BusinessValidationException.class);

            verify(userMovieRepository, never()).save(any());
        }
    }

    // ==================== CHANGE MOVIE STATUS TESTS ====================

    @Nested
    @DisplayName("changeMovieStatus() method")
    class ChangeMovieStatusTests {

        @Nested
        @DisplayName("When UserMovie record exists")
        class WhenRecordExists {

            @Test
            @DisplayName("Should add movie to favorites")
            void shouldAddMovieToFavorites() {
                // Arrange
                MovieStatusRequest request = new MovieStatusRequest(
                        TEST_MOVIE_ID,
                        MovieStatusRequest.Category.FAVORITE,
                        MovieStatusRequest.Action.ADD
                );

                testUserMovie.setFavorite(false);
                when(userMovieRepository.findByMovieIdAndUserUsername(TEST_MOVIE_ID, TEST_USERNAME))
                        .thenReturn(Optional.of(testUserMovie));


                when(userMovieRepository.save(any(UserMovie.class)))
                        .thenAnswer(inv -> inv.getArgument(0));

                // Act
                MovieStatus result = userMovieService.changeMovieStatus(request, TEST_USERNAME);

                // Assert
                assertThat(result.getIsFavorite()).isTrue();
                verify(userMovieRepository).save(argThat(um -> um.isFavorite()));
            }

            @Test
            @DisplayName("Should remove movie from favorites")
            void shouldRemoveMovieFromFavorites() {
                // Arrange
                MovieStatusRequest request = new MovieStatusRequest(
                        TEST_MOVIE_ID,
                        MovieStatusRequest.Category.FAVORITE,
                        MovieStatusRequest.Action.REMOVE
                );

                testUserMovie.setFavorite(true);
                testUserMovie.setWatched(true);  // ← Keep one flag true to prevent deletion


                when(userMovieRepository.findByMovieIdAndUserUsername(TEST_MOVIE_ID, TEST_USERNAME))
                        .thenReturn(Optional.of(testUserMovie));

                when(userMovieRepository.save(any(UserMovie.class)))
                        .thenAnswer(inv -> inv.getArgument(0));

                // Act
                MovieStatus result = userMovieService.changeMovieStatus(request, TEST_USERNAME);

                // Assert
                assertThat(result.getIsFavorite()).isFalse();
                assertThat(result.getWatched()).isTrue();  // Verify other flag preserved
                verify(userMovieRepository).save(any());
                verify(userMovieRepository, never()).delete(any());
            }

            @Test
            @DisplayName("Should add movie to watched")
            void shouldAddMovieToWatched() {
                // Arrange
                MovieStatusRequest request = new MovieStatusRequest(
                        TEST_MOVIE_ID,
                        MovieStatusRequest.Category.WATCHED,
                        MovieStatusRequest.Action.ADD
                );

                when(userMovieRepository.findByMovieIdAndUserUsername(TEST_MOVIE_ID, TEST_USERNAME))
                        .thenReturn(Optional.of(testUserMovie));
                when(userMovieRepository.save(any(UserMovie.class)))
                        .thenAnswer(inv -> inv.getArgument(0));

                // Act
                MovieStatus result = userMovieService.changeMovieStatus(request, TEST_USERNAME);

                // Assert
                assertThat(result.getWatched()).isTrue();
            }

            @Test
            @DisplayName("Should add movie to watchlist")
            void shouldAddMovieToWatchlist() {
                // Arrange
                MovieStatusRequest request = new MovieStatusRequest(
                        TEST_MOVIE_ID,
                        MovieStatusRequest.Category.WATCHLIST,
                        MovieStatusRequest.Action.ADD
                );

                when(userMovieRepository.findByMovieIdAndUserUsername(TEST_MOVIE_ID, TEST_USERNAME))
                        .thenReturn(Optional.of(testUserMovie));
                when(userMovieRepository.save(any(UserMovie.class)))
                        .thenAnswer(inv -> inv.getArgument(0));

                // Act
                MovieStatus result = userMovieService.changeMovieStatus(request, TEST_USERNAME);

                // Assert
                assertThat(result.getInWatchlist()).isTrue();
            }

            @Test
            @DisplayName("Should delete record when all flags become false")
            void shouldDeleteRecordWhenAllFlagsFalse() {
                // Arrange
                MovieStatusRequest request = new MovieStatusRequest(
                        TEST_MOVIE_ID,
                        MovieStatusRequest.Category.FAVORITE,
                        MovieStatusRequest.Action.REMOVE
                );

                // Only favorite is true, removing it makes all flags false
                testUserMovie.setFavorite(true);
                testUserMovie.setWatched(false);
                testUserMovie.setInWatchlist(false);

                when(userMovieRepository.findByMovieIdAndUserUsername(TEST_MOVIE_ID, TEST_USERNAME))
                        .thenReturn(Optional.of(testUserMovie));

                // Act
                MovieStatus result = userMovieService.changeMovieStatus(request, TEST_USERNAME);

                // Assert
                assertThat(result).isEqualTo(MovieStatus.defaultStatus());
                verify(userMovieRepository).delete(testUserMovie);
                verify(userMovieRepository, never()).save(any());
            }

            @Test
            @DisplayName("Should not delete record when at least one flag is true")
            void shouldNotDeleteRecordWhenOneFlagTrue() {
                // Arrange
                MovieStatusRequest request = new MovieStatusRequest(
                        TEST_MOVIE_ID,
                        MovieStatusRequest.Category.FAVORITE,
                        MovieStatusRequest.Action.REMOVE
                );

                testUserMovie.setFavorite(true);
                testUserMovie.setWatched(true); // This keeps the record alive
                testUserMovie.setInWatchlist(false);

                when(userMovieRepository.findByMovieIdAndUserUsername(TEST_MOVIE_ID, TEST_USERNAME))
                        .thenReturn(Optional.of(testUserMovie));
                when(userMovieRepository.save(any(UserMovie.class)))
                        .thenAnswer(inv -> inv.getArgument(0));

                // Act
                MovieStatus result = userMovieService.changeMovieStatus(request, TEST_USERNAME);

                // Assert
                verify(userMovieRepository, never()).delete(any());
                verify(userMovieRepository).save(any());
                assertThat(result.getWatched()).isTrue();
            }
        }
//
//        @Nested
//        @DisplayName("When UserMovie record does not exist")
//        class u {
//
//            @Test
//            @DisplayName("Should create new record when movie exists locally")
//            void shouldCreateNewRecordWhenMovieExists() {
//                // Arrange
//                MovieStatusRequest request = new MovieStatusRequest(
//                        TEST_MOVIE_ID,
//                        MovieStatusRequest.Category.FAVORITE,
//                        MovieStatusRequest.Action.ADD
//                );
//
//                when(userMovieRepository.findByMovieIdAndUserUsername(TEST_MOVIE_ID, TEST_USERNAME))
//                        .thenReturn(Optional.empty());
//                when(userService.getUserByUsername(TEST_USERNAME))
//                        .thenReturn(testUser);
//                when(movieService.findById(TEST_MOVIE_ID))
//                        .thenReturn(Optional.of(testMovie));
//                when(userMovieRepository.save(any(UserMovie.class)))
//                        .thenAnswer(inv -> {
//                            UserMovie um = inv.getArgument(0);
//                            um.setFavorite(true);
//                            return um;
//                        });
//
//                // Act
//                MovieStatus result = userMovieService.changeMovieStatus(request, TEST_USERNAME);
//
//                // Assert
//                assertThat(result.isFavorite()).isTrue();
//                verify(userMovieRepository).save(any(UserMovie.class));
//            }
//
//            @Test
//            @DisplayName("Should publish event when movie does not exist locally")
//            void shouldPublishEventWhenMovieNotExists() {
//                // Arrange
//                MovieStatusRequest request = new MovieStatusRequest(
//                        TEST_MOVIE_ID,
//                        MovieStatusRequest.Category.FAVORITE,
//                        MovieStatusRequest.Action.ADD
//                );
//
//                when(userMovieRepository.findByMovieIdAndUserUsername(TEST_MOVIE_ID, TEST_USERNAME))
//                        .thenReturn(Optional.empty());
//                when(userService.getUserByUsername(TEST_USERNAME))
//                        .thenReturn(testUser);
//                when(movieService.findById(TEST_MOVIE_ID))
//                        .thenReturn(Optional.empty());
//
//                // Act
//                MovieStatus result = userMovieService.changeMovieStatus(request, TEST_USERNAME);
//
//                // Assert - should return optimistic status
//                assertThat(result.isFavorite()).isTrue();
//                verify(userMovieEventPublisher).publishUserMovieEvent(TEST_USERNAME, request);
//            }
//
//            @Test
//            @DisplayName("Should return optimistic status for WATCHED category")
//            void shouldReturnOptimisticStatusForWatched() {
//                // Arrange
//                MovieStatusRequest request = new MovieStatusRequest(
//                        TEST_MOVIE_ID,
//                        MovieStatusRequest.Category.WATCHED,
//                        MovieStatusRequest.Action.ADD
//                );
//
//                when(userMovieRepository.findByMovieIdAndUserUsername(TEST_MOVIE_ID, TEST_USERNAME))
//                        .thenReturn(Optional.empty());
//                when(userService.getUserByUsername(TEST_USERNAME))
//                        .thenReturn(testUser);
//                when(movieService.findById(TEST_MOVIE_ID))
//                        .thenReturn(Optional.empty());
//
//                // Act
//                MovieStatus result = userMovieService.changeMovieStatus(request, TEST_USERNAME);
//
//                // Assert
//                assertThat(result.isFavorite()).isFalse();
//                assertThat(result.isWatched()).isTrue();
//                assertThat(result.isInWatchlist()).isFalse();
//            }
//
//            @Test
//            @DisplayName("Should return optimistic status for WATCHLIST category")
//            void shouldReturnOptimisticStatusForWatchlist() {
//                // Arrange
//                MovieStatusRequest request = new MovieStatusRequest(
//                        TEST_MOVIE_ID,
//                        MovieStatusRequest.Category.WATCHLIST,
//                        MovieStatusRequest.Action.ADD
//                );
//
//                when(userMovieRepository.findByMovieIdAndUserUsername(TEST_MOVIE_ID, TEST_USERNAME))
//                        .thenReturn(Optional.empty());
//                when(userService.getUserByUsername(TEST_USERNAME))
//                        .thenReturn(testUser);
//                when(movieService.findById(TEST_MOVIE_ID))
//                        .thenReturn(Optional.empty());
//
//                // Act
//                MovieStatus result = userMovieService.changeMovieStatus(request, TEST_USERNAME);
//
//                // Assert
//                assertThat(result.isFavorite()).isFalse();
//                assertThat(result.isWatched()).isFalse();
//                assertThat(result.isInWatchlist()).isTrue();
//            }
//        }
//    }
//
//    // ==================== GET USER MOVIES TESTS ====================
//
//    @Nested
//    @DisplayName("getUserFavoriteMovies() method")
//    class GetUserFavoriteMoviesTests {
//
//        @Test
//        @DisplayName("Should return paginated favorite movies")
//        void shouldReturnPaginatedFavoriteMovies() {
//            // Arrange
//            Pageable pageable = PageRequest.of(0, 20);
//            MovieResponse movieResponse = new MovieResponse();
//            Page<MovieResponse> page = new PageImpl<>(List.of(movieResponse), pageable, 1);
//
//            when(userMovieRepository.findUserFavoriteMovies(TEST_USERNAME, pageable))
//                    .thenReturn(page);
//
//            // Act
//            MovieListResponse result = userMovieService.getUserFavoriteMovies(TEST_USERNAME, pageable);
//
//            // Assert
//            assertThat(result.getResults()).hasSize(1);
//            assertThat(result.getTotalPages()).isEqualTo(1L);
//            assertThat(result.getPage()).isZero();
//            assertThat(result.getTotalResults()).isEqualTo(1L);
//        }
//
//        @Test
//        @DisplayName("Should return empty response when no favorites")
//        void shouldReturnEmptyResponseWhenNoFavorites() {
//            // Arrange
//            Pageable pageable = PageRequest.of(0, 20);
//            Page<MovieResponse> emptyPage = new PageImpl<>(List.of(), pageable, 0);
//
//            when(userMovieRepository.findUserFavoriteMovies(TEST_USERNAME, pageable))
//                    .thenReturn(emptyPage);
//
//            // Act
//            MovieListResponse result = userMovieService.getUserFavoriteMovies(TEST_USERNAME, pageable);
//
//            // Assert
//            assertThat(result.getResults()).isEmpty();
//            assertThat(result.getTotalResults()).isZero();
//        }
//    }
//
//    @Nested
//    @DisplayName("getUserWatchedMovies() method")
//    class GetUserWatchedMoviesTests {
//
//        @Test
//        @DisplayName("Should return paginated watched movies")
//        void shouldReturnPaginatedWatchedMovies() {
//            // Arrange
//            Pageable pageable = PageRequest.of(0, 20);
//            MovieResponse movieResponse = new MovieResponse();
//            Page<MovieResponse> page = new PageImpl<>(List.of(movieResponse), pageable, 1);
//
//            when(userMovieRepository.findUserWatchedMovies(TEST_USERNAME, pageable))
//                    .thenReturn(page);
//
//            // Act
//            MovieListResponse result = userMovieService.getUserWatchedMovies(TEST_USERNAME, pageable);
//
//            // Assert
//            assertThat(result.getResults()).hasSize(1);
//            assertThat(result.getTotalPages()).isEqualTo(1L);
//        }
//    }
//
//    @Nested
//    @DisplayName("getUserWatchlistMovies() method")
//    class GetUserWatchlistMoviesTests {
//
//        @Test
//        @DisplayName("Should return paginated watchlist movies")
//        void shouldReturnPaginatedWatchlistMovies() {
//            // Arrange
//            Pageable pageable = PageRequest.of(0, 20);
//            MovieResponse movieResponse = new MovieResponse();
//            Page<MovieResponse> page = new PageImpl<>(List.of(movieResponse), pageable, 1);
//
//            when(userMovieRepository.findUserWatchlistMovies(TEST_USERNAME, pageable))
//                    .thenReturn(page);
//
//            // Act
//            MovieListResponse result = userMovieService.getUserWatchlistMovies(TEST_USERNAME, pageable);
//
//            // Assert
//            assertThat(result.getResults()).hasSize(1);
//            assertThat(result.getTotalPages()).isEqualTo(1L);
//        }
//    }
//
//    // ==================== GET USER MOVIES STATS TESTS ====================
//
//    @Nested
//    @DisplayName("getUserMoviesStats() methods")
//    class GetUserMoviesStatsTests {
//
//        @Test
//        @DisplayName("Should return user movie statistics")
//        void shouldReturnUserMovieStatistics() {
//            // Arrange
//            UserMovieTvStats expectedStats = new UserMovieTvStats(10L, 5L, 3L);
//            when(userMovieRepository.getUserMovieStats(TEST_USERNAME))
//                    .thenReturn(expectedStats);
//
//            // Act
//            UserMovieTvStats result = userMovieService.getUserMoviesStats(TEST_USERNAME);
//
//            // Assert
//            assertThat(result.getFavoriteCount()).isEqualTo(10L);
//            assertThat(result.getWatchedCount()).isEqualTo(5L);
//            assertThat(result.getWatchlistCount()).isEqualTo(3L);
//        }
//
//        @Test
//        @DisplayName("Should return movie status by movieId and username")
//        void shouldReturnMovieStatusByIdAndUsername() {
//            // Arrange
//            MovieStatus expectedStatus = new MovieStatus(true, false, true);
//            when(userMovieRepository.findMovieStatus(TEST_MOVIE_ID, TEST_USERNAME))
//                    .thenReturn(Optional.of(expectedStatus));
//
//            // Act
//            MovieStatus result = userMovieService.getUserMoviesStats(TEST_MOVIE_ID, TEST_USERNAME);
//
//            // Assert
//            assertThat(result.isFavorite()).isTrue();
//            assertThat(result.isWatched()).isFalse();
//            assertThat(result.isInWatchlist()).isTrue();
//        }
//
//        @Test
//        @DisplayName("Should return default status when movie status not found")
//        void shouldReturnDefaultStatusWhenNotFound() {
//            // Arrange
//            when(userMovieRepository.findMovieStatus(TEST_MOVIE_ID, TEST_USERNAME))
//                    .thenReturn(Optional.empty());
//
//            // Act
//            MovieStatus result = userMovieService.getUserMoviesStats(TEST_MOVIE_ID, TEST_USERNAME);
//
//            // Assert
//            assertThat(result).isEqualTo(MovieStatus.defaultStatus());
//        }
//    }
//
//    // ==================== EXISTS BY USERNAME AND MOVIE ID TESTS ====================
//
//    @Nested
//    @DisplayName("existsByUserUsernameAndMovieId() method")
//    class ExistsByUserUsernameAndMovieIdTests {
//
//        @Test
//        @DisplayName("Should return true when record exists")
//        void shouldReturnTrueWhenExists() {
//            // Arrange
//            when(userMovieRepository.existsByUserUsernameAndMovieId(TEST_USERNAME, TEST_MOVIE_ID))
//                    .thenReturn(true);
//
//            // Act
//            Boolean result = userMovieService.existsByUserUsernameAndMovieId(TEST_USERNAME, TEST_MOVIE_ID);
//
//            // Assert
//            assertThat(result).isTrue();
//        }
//
//        @Test
//        @DisplayName("Should return false when record does not exist")
//        void shouldReturnFalseWhenNotExists() {
//            // Arrange
//            when(userMovieRepository.existsByUserUsernameAndMovieId(TEST_USERNAME, 99999L))
//                    .thenReturn(false);
//
//            // Act
//            Boolean result = userMovieService.existsByUserUsernameAndMovieId(TEST_USERNAME, 99999L);
//
//            // Assert
//            assertThat(result).isFalse();
//        }
//    }
//
//    // ==================== FIND BY USERNAME AND MOVIE IDS TESTS ====================
//
//    @Nested
//    @DisplayName("findByUsernameAndMovieIds() method")
//    class FindByUsernameAndMovieIdsTests {
//
//        @Test
//        @DisplayName("Should return status map for multiple movies")
//        void shouldReturnStatusMapForMultipleMovies() {
//            // Arrange
//            List<Integer> movieIds = List.of(123, 456, 789);
//
//            Movie movie1 = new Movie();
//            movie1.setId(123L);
//            Movie movie2 = new Movie();
//            movie2.setId(456L);
//
//            UserMovie um1 = new UserMovie();
//            um1.setMovie(movie1);
//            um1.setFavorite(true);
//            um1.setWatched(false);
//            um1.setInWatchlist(false);
//
//            UserMovie um2 = new UserMovie();
//            um2.setMovie(movie2);
//            um2.setFavorite(false);
//            um2.setWatched(true);
//            um2.setInWatchlist(true);
//
//            when(userMovieRepository.findByUserUsernameAndMovieIdIn(TEST_USERNAME, movieIds))
//                    .thenReturn(List.of(um1, um2));
//
//            // Act
//            MovieStatusList result = userMovieService.findByUsernameAndMovieIds(TEST_USERNAME, movieIds);
//
//            // Assert
//            assertThat(result.getMoviesStatus()).containsKey(123L);
//            assertThat(result.getMoviesStatus()).containsKey(456L);
//            assertThat(result.getMoviesStatus().get(123L).isFavorite()).isTrue();
//            assertThat(result.getMoviesStatus().get(456L).isWatched()).isTrue();
//        }
//
//        @Test
//        @DisplayName("Should return empty map when no movies found")
//        void shouldReturnEmptyMapWhenNoMoviesFound() {
//            // Arrange
//            List<Integer> movieIds = List.of(999, 888);
//
//            when(userMovieRepository.findByUserUsernameAndMovieIdIn(TEST_USERNAME, movieIds))
//                    .thenReturn(List.of());
//
//            // Act
//            MovieStatusList result = userMovieService.findByUsernameAndMovieIds(TEST_USERNAME, movieIds);
//
//            // Assert
//            assertThat(result.getMoviesStatus()).isEmpty();
//        }
//    }
    }
}