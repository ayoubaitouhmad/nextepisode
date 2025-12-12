package com.nextepisode.user_service;


import com.nextepisode.user_service.entity.movie.Movie;
import com.nextepisode.user_service.entity.movie.MovieGenre;
import com.nextepisode.user_service.entity.user.User;
import com.nextepisode.user_service.entity.user.UserMovie;
import com.nextepisode.user_service.entity.user.UserMovieId;
import com.nextepisode.user_service.repo.GenreRepository;
import com.nextepisode.user_service.repo.MovieRepository;
import com.nextepisode.user_service.repo.UserMovieRepository;
import com.nextepisode.user_service.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(UserRepository userRepository,
                               MovieRepository movieRepository,
                               GenreRepository genreRepository,
                               UserMovieRepository userMovieRepository) {
        return args -> {

            if (genreRepository.count() > 0) {
                log.info("Database already seeded. Skipping...");
                return;
            }

            // Clear existing data (optional - comment out if you want to preserve data)
            userMovieRepository.deleteAll();
            movieRepository.deleteAll();
            genreRepository.deleteAll();
            userRepository.deleteAll();

            log.info("Starting database seeding...");
            List<MovieGenre> genres = seedGenres(genreRepository);
            List<User> users = seedUsers(userRepository);
            List<Movie> movies = seedMovies(genres, movieRepository);
            seedUserMovies(users, movies, userMovieRepository);
            log.info("Created {} genres, {} users, {} movies", genres.size(), users.size(), movies.size());

        };
    }

    private List<MovieGenre> seedGenres(GenreRepository genreRepository) {
        log.info("Seeding genres...");

        List<MovieGenre> genres = Arrays.asList(
                new MovieGenre(28L, "Action"),
                new MovieGenre(12L, "Adventure"),
                new MovieGenre(16L, "Animation"),
                new MovieGenre(35L, "Comedy"),
                new MovieGenre(80L, "Crime"),
                new MovieGenre(99L, "Documentary"),
                new MovieGenre(18L, "Drama"),
                new MovieGenre(10751L, "Family"),
                new MovieGenre(14L, "Fantasy"),
                new MovieGenre(36L, "History"),
                new MovieGenre(27L, "Horror"),
                new MovieGenre(10402L, "Music"),
                new MovieGenre(9648L, "Mystery"),
                new MovieGenre(10749L, "Romance"),
                new MovieGenre(878L, "Science Fiction"),
                new MovieGenre(10770L, "TV Movie"),
                new MovieGenre(53L, "Thriller"),
                new MovieGenre(10752L, "War"),
                new MovieGenre(37L, "Western")
        );

        return genreRepository.saveAll(genres);
    }

    private List<User> seedUsers(UserRepository userRepository) {
        log.info("Seeding users...");

        List<User> users = Arrays.asList(
                createUser("ayoubaitx", "ayoub", "ait", "ayoub.ait@example.com", "Movie enthusiast and critic", "New York, USA"),
                createUser("jane_smith", "Jane", "Smith", "jane.smith@example.com", "Film director and writer", "Los Angeles, USA"),
                createUser("mike_wilson", "Mike", "Wilson", "mike.wilson@example.com", "Casual movie watcher", "Chicago, USA"),
                createUser("sarah_jones", "Sarah", "Jones", "sarah.jones@example.com", "Horror movie fanatic", "Austin, USA"),
                createUser("david_brown", "David", "Brown", "david.brown@example.com", "Sci-fi lover", "Seattle, USA"),
                createUser("emily_davis", "Emily", "Davis", "emily.davis@example.com", "Rom-com addict", "Miami, USA"),
                createUser("chris_miller", "Chris", "Miller", "chris.miller@example.com", "Action movie buff", "Denver, USA"),
                createUser("lisa_garcia", "Lisa", "Garcia", "lisa.garcia@example.com", "Documentary enthusiast", "San Francisco, USA"),
                createUser("tom_martinez", "Tom", "Martinez", "tom.martinez@example.com", "Classic film collector", "Boston, USA"),
                createUser("anna_taylor", "Anna", "Taylor", "anna.taylor@example.com", "Animation lover", "Portland, USA")
        );

        return userRepository.saveAll(users);
    }

    private User createUser(String username, String firstName, String lastName, String email, String bio, String location) {
        Instant now = Instant.now();
        Instant oneYearAgo = now.minus(Duration.ofDays(365));

        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setBio(bio);
        user.setLocation(location);
        user.setRole("USER");
        user.setEmailVerified(true);
        user.setIsActive(true);
        user.setIsDirty(false);
        user.setNotificationsEnabled(true);
        user.setPreferredLanguage("en");
        user.setTimezone("UTC");
        user.setDateOfBirth(randomDateOfBirth());
        user.setLastLogin(Instant.now());
        return user;
    }

    private LocalDate randomDateOfBirth() {
        Random random = new Random();
        int minAge = 18;
        int maxAge = 60;
        int age = minAge + random.nextInt(maxAge - minAge);
        return LocalDate.now().minusYears(age).minusDays(random.nextInt(365));
    }

    private List<Movie> seedMovies(List<MovieGenre> genres, MovieRepository movieRepository) {
        log.info("Seeding movies...");

        Map<String, MovieGenre> genreMap = new HashMap<>();
        for (MovieGenre genre : genres) {
            genreMap.put(genre.getName(), genre);
        }

        List<Movie> movies = new ArrayList<>();

        // Movie data: id, title, posterPath, releaseYear, genre names
        Object[][] movieData = {
                {550L, "Fight Club", "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg", 1999, Arrays.asList("Drama", "Thriller")},
                {680L, "Pulp Fiction", "/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg", 1994, Arrays.asList("Crime", "Thriller")},
                {155L, "The Dark Knight", "/qJ2tW6WMUDux911r6m7haRef0WH.jpg", 2008, Arrays.asList("Action", "Crime", "Drama")},
                {238L, "The Godfather", "/3bhkrj58Vtu7enYsRolD1fZdja1.jpg", 1972, Arrays.asList("Crime", "Drama")},
                {13L, "Forrest Gump", "/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg", 1994, Arrays.asList("Comedy", "Drama", "Romance")},
                {278L, "The Shawshank Redemption", "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg", 1994, Arrays.asList("Drama", "Crime")},
                {27205L, "Inception", "/oYuLEt3zVCKq57qu2F8dT7NIa6f.jpg", 2010, Arrays.asList("Action", "Science Fiction", "Adventure")},
                {603L, "The Matrix", "/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg", 1999, Arrays.asList("Action", "Science Fiction")},
                {157336L, "Interstellar", "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg", 2014, Arrays.asList("Adventure", "Drama", "Science Fiction")},
                {122L, "The Lord of the Rings: The Return of the King", "/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg", 2003, Arrays.asList("Adventure", "Fantasy", "Action")},
                {424L, "Schindler's List", "/sF1U4EUQS8YHUYjNl3pMGNIQyr0.jpg", 1993, Arrays.asList("Drama", "History", "War")},
                {429L, "The Good, the Bad and the Ugly", "/bX2xnavhMYjWDoZp1VM6VnU1xwe.jpg", 1966, Arrays.asList("Western", "Action")},
                {769L, "GoodFellas", "/aKuFiU82s5ISJpGZp7YkIr3kCUd.jpg", 1990, Arrays.asList("Crime", "Drama")},
                {497L, "The Green Mile", "/velWPhVMQeQKcxggNEU8YmIo52R.jpg", 1999, Arrays.asList("Fantasy", "Drama", "Crime")},
                {389L, "12 Angry Men", "/ow3wq89wM8qd5X7hWKxiRfsFf9C.jpg", 1957, Arrays.asList("Drama")},
                {129L, "Spirited Away", "/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg", 2001, Arrays.asList("Animation", "Family", "Fantasy")},
                {539L, "Psycho", "/yz4QVqPx3h1hD1DfqqQkCq3rmxW.jpg", 1960, Arrays.asList("Horror", "Thriller")},
                {346L, "Seven Samurai", "/8OKmBV5BUFzmozIC3pPWKHy17kx.jpg", 1954, Arrays.asList("Action", "Drama")},
                {807L, "Se7en", "/6yoghtyTpznpBik8EngEmJskVUO.jpg", 1995, Arrays.asList("Crime", "Mystery", "Thriller")},
                {11L, "Star Wars", "/6FfCtAuVAW8XJjZ7eWeLibRLWTw.jpg", 1977, Arrays.asList("Adventure", "Action", "Science Fiction")},
                {120L, "The Lord of the Rings: The Fellowship of the Ring", "/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg", 2001, Arrays.asList("Adventure", "Fantasy", "Action")},
                {598L, "City of God", "/k7eYdWvhYQyRQoU2TB2A2Xu2TfD.jpg", 2002, Arrays.asList("Crime", "Drama")},
                {274L, "The Silence of the Lambs", "/uS9m8OBk1A8eM9I042bx8XXpqAq.jpg", 1991, Arrays.asList("Crime", "Drama", "Thriller")},
                {637L, "Life Is Beautiful", "/74hLDKjD5aGYOotO6esUVaeISa2.jpg", 1997, Arrays.asList("Comedy", "Drama", "Romance")},
                {510L, "One Flew Over the Cuckoo's Nest", "/3jcbDmRFiQ83drXNOvRDeKHxS0C.jpg", 1975, Arrays.asList("Drama")},
                {240L, "The Godfather Part II", "/hek3koDUyRQq7gkByPdQkNT1H3c.jpg", 1974, Arrays.asList("Crime", "Drama")},
                {244786L, "Whiplash", "/oPxnRhyAIzJKGUEdSiwTJQBa3NM.jpg", 2014, Arrays.asList("Drama", "Music")},
                {372058L, "Your Name.", "/q719jXXEzOoYaps6babgKnONONX.jpg", 2016, Arrays.asList("Animation", "Romance", "Drama")},
                {324857L, "Spider-Man: Into the Spider-Verse", "/iiZZdoQBEYBv6id8su7ImL0oCbD.jpg", 2018, Arrays.asList("Animation", "Action", "Adventure")},
                {569094L, "Spider-Man: Across the Spider-Verse", "/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg", 2023, Arrays.asList("Animation", "Action", "Adventure")},
                {299536L, "Avengers: Infinity War", "/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg", 2018, Arrays.asList("Action", "Adventure", "Science Fiction")},
                {299534L, "Avengers: Endgame", "/or06FN3Dka5tukK1e9sl16pB3iy.jpg", 2019, Arrays.asList("Action", "Adventure", "Science Fiction")},
                {438631L, "Dune", "/d5NXSklXo0qyIYkgV94XAgMIckC.jpg", 2021, Arrays.asList("Science Fiction", "Adventure")},
                {693134L, "Dune: Part Two", "/8b8R8l88Qje9dn9OE8PY05Nxl1X.jpg", 2024, Arrays.asList("Science Fiction", "Adventure")},
                {346698L, "Barbie", "/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg", 2023, Arrays.asList("Comedy", "Adventure", "Fantasy")},
                {872585L, "Oppenheimer", "/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg", 2023, Arrays.asList("Drama", "History")},
                {572802L, "Aquaman and the Lost Kingdom", "/7lTnXOy0iNtBAdRP3TZvaKJ77F6.jpg", 2023, Arrays.asList("Action", "Adventure", "Fantasy")},
                {466420L, "Killers of the Flower Moon", "/dB6Krk806zeqd0YNp2ngQ9zXteH.jpg", 2023, Arrays.asList("Crime", "Drama", "History")},
                {467244L, "The Zone of Interest", "/hUu9zyZmDd8VZegKi1iK1Vk0RYS.jpg", 2023, Arrays.asList("Drama", "History", "War")},
                {792307L, "Poor Things", "/kCGlIMHnOm8JPXq3rXM6c5wMxcT.jpg", 2023, Arrays.asList("Science Fiction", "Romance", "Comedy")},
                {787699L, "Wonka", "/qhb1qOilapbapxWQn9jtRCMwXJF.jpg", 2023, Arrays.asList("Comedy", "Family", "Fantasy")},
                {940551L, "Migration", "/ldfCF9RhR40mppkzmftxapaHeTo.jpg", 2023, Arrays.asList("Animation", "Family", "Adventure", "Comedy")},
                {748783L, "The Garfield Movie", "/p6AbOJvMQhBmfKXvfw2O9lMpRcZ.jpg", 2024, Arrays.asList("Animation", "Comedy", "Family", "Adventure")},
                {823464L, "Godzilla x Kong: The New Empire", "/z1p34vh7dEOnLDmyCrlUVLuoDzd.jpg", 2024, Arrays.asList("Action", "Science Fiction", "Adventure")},
                {1011985L, "Kung Fu Panda 4", "/kDp1vUBnMpe8ak4rjgl3cLELqjU.jpg", 2024, Arrays.asList("Animation", "Action", "Family", "Comedy")},
                {653346L, "Kingdom of the Planet of the Apes", "/gKkl37BQuKTanygYQG1pyYgLVgf.jpg", 2024, Arrays.asList("Science Fiction", "Adventure", "Action")},
                {1022789L, "Inside Out 2", "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg", 2024, Arrays.asList("Animation", "Family", "Drama", "Comedy")},
                {573435L, "Bad Boys: Ride or Die", "/nP6RliHjxsz4irTKsxe8FRhKZYl.jpg", 2024, Arrays.asList("Action", "Comedy", "Crime")},
                {929590L, "Civil War", "/sh7Rg8Er3tFcN9BpKIPOMvALgZd.jpg", 2024, Arrays.asList("War", "Action", "Drama")},
                {1111873L, "Despicable Me 4", "/wWba3TaojhK7NdycRhoQpsG0FaH.jpg", 2024, Arrays.asList("Animation", "Family", "Comedy")}
        };

        for (Object[] data : movieData) {
            Movie movie = new Movie();
            movie.setId((Long) data[0]);
            movie.setTitle((String) data[1]);
            movie.setPosterPath((String) data[2]);
            movie.setReleaseDate(Instant.parse((int) data[3] + "-06-15T00:00:00Z"));

            List<String> genreNames = (List<String>) data[4];
            List<MovieGenre> movieGenres = new ArrayList<>();
            for (String genreName : genreNames) {
                MovieGenre genre = genreMap.get(genreName);
                if (genre != null) {
                    movieGenres.add(genre);
                }
            }
            movie.setGenres(movieGenres);

            movies.add(movie);
        }

        return movieRepository.saveAll(movies);
    }

    private void seedUserMovies(List<User> users, List<Movie> movies, UserMovieRepository userMovieRepository) {
        log.info("Seeding user-movie relationships...");

        Random random = new Random();
        List<UserMovie> userMovies = new ArrayList<>();

        for (User user : users) {
            // Each user has 5-15 random movies in their collection
            int movieCount = 5 + random.nextInt(11);
            List<Movie> shuffledMovies = new ArrayList<>(movies);
            Collections.shuffle(shuffledMovies);

            for (int i = 0; i < Math.min(movieCount, shuffledMovies.size()); i++) {
                Movie movie = shuffledMovies.get(i);

                UserMovie userMovie = new UserMovie();
                userMovie.setId(new UserMovieId(user.getUsername(), movie.getId()));
                userMovie.setUser(user);
                userMovie.setMovie(movie);

                // Randomly set flags
                boolean isFavorite = random.nextDouble() < 0.3; // 30% chance
                boolean inWatchlist = random.nextDouble() < 0.4; // 40% chance
                boolean watched = random.nextDouble() < 0.6; // 60% chance

                userMovie.setFavorite(isFavorite);
                userMovie.setInWatchlist(inWatchlist && !watched); // Not in watchlist if already watched
                userMovie.setWatched(watched);

                if (watched) {
                    userMovie.setWatchedAt(randomPastInstant(365));
                }

                userMovie.setCreatedAt(randomPastInstant(400));

                userMovies.add(userMovie);
            }
        }

        userMovieRepository.saveAll(userMovies);
        log.info("Created {} user-movie relationships", userMovies.size());
    }

    private Instant randomPastInstant(int maxDaysAgo) {
        Random random = new Random();
        long daysAgo = random.nextInt(maxDaysAgo);
        return Instant.now().minus(daysAgo, ChronoUnit.DAYS);
    }
}
