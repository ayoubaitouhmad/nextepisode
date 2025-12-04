// MovieDto interface matching your backend API response
export interface MovieDto {
  id: number;
  tmdbId: number;
  title: string;
  overview: string;
  posterPath: string;
  backdropPath: string;
  releaseDate: string;
  voteAverage: number;
  voteCount: number;
  genres: string[];
  type: 'movie' | 'tv';
}

// Updated MovieStatistics interface as requested
export interface MovieStatistics {
  favoriteCount: number;
  watchedCount: number;
  watchlistCount: number;
}

// Grouped movies by type for display
export interface GroupedMovies {
  movies: MovieDto[];
  tvSeries: MovieDto[];
}

// Static sample data for development/testing
export const STATIC_FAVORITE_MOVIES: MovieDto[] = [
  {
    id: 1,
    tmdbId: 550,
    title: 'Fight Club',
    overview: 'A depressed man suffering from insomnia meets a strange soap salesman.',
    posterPath: '/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg',
    backdropPath: '/hZkgoQYus5vegHoetLkCJzb17zJ.jpg',
    releaseDate: '1999-10-15',
    voteAverage: 8.4,
    voteCount: 26280,
    genres: ['Drama', 'Thriller'],
    type: 'movie'
  },
  {
    id: 2,
    tmdbId: 680,
    title: 'Pulp Fiction',
    overview: 'The lives of two mob hitmen, a boxer, a gangster and his wife intertwine.',
    posterPath: '/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg',
    backdropPath: '/suaEOtk1N1sgg2MTM7oZd2cfVp3.jpg',
    releaseDate: '1994-09-10',
    voteAverage: 8.5,
    voteCount: 24521,
    genres: ['Crime', 'Thriller'],
    type: 'movie'
  },
  {
    id: 3,
    tmdbId: 238,
    title: 'The Godfather',
    overview: 'The aging patriarch of an organized crime dynasty transfers control to his son.',
    posterPath: '/3bhkrj58Vtu7enYsRolD1fZdja1.jpg',
    backdropPath: '/rSPw7tgCH9c6NqICZef4kZjFOQ5.jpg',
    releaseDate: '1972-03-14',
    voteAverage: 8.7,
    voteCount: 18234,
    genres: ['Drama', 'Crime'],
    type: 'movie'
  },
  {
    id: 4,
    tmdbId: 278,
    title: 'The Shawshank Redemption',
    overview: 'Framed in the 1940s for a double murder, banker Andy Dufresne begins a new life at Shawshank prison.',
    posterPath: '/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg',
    backdropPath: '/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg',
    releaseDate: '1994-09-23',
    voteAverage: 8.7,
    voteCount: 23456,
    genres: ['Drama', 'Crime'],
    type: 'movie'
  },
  {
    id: 5,
    tmdbId: 1399,
    title: 'Game of Thrones',
    overview: 'Seven noble families fight for control of the mythical land of Westeros.',
    posterPath: '/u3bZgnGQ9T01sWNhyveQz0wH0Hl.jpg',
    backdropPath: '/suopoADq0k8YZr4dQXcU6pToj6s.jpg',
    releaseDate: '2011-04-17',
    voteAverage: 8.4,
    voteCount: 19234,
    genres: ['Drama', 'Fantasy', 'Action'],
    type: 'tv'
  },
  {
    id: 6,
    tmdbId: 1402,
    title: 'The Walking Dead',
    overview: 'Sheriff Deputy Rick Grimes wakes up from a coma to learn the world is overrun by zombies.',
    posterPath: '/xf9wuDcqlUPWABZNeDKPbZUjWx0.jpg',
    backdropPath: '/wvdWb5kTQipdMDqCclC6Y3zr4j3.jpg',
    releaseDate: '2010-10-31',
    voteAverage: 8.1,
    voteCount: 15678,
    genres: ['Drama', 'Horror', 'Action'],
    type: 'tv'
  }
];

export const STATIC_WATCHED_MOVIES: MovieDto[] = [
  {
    id: 7,
    tmdbId: 157336,
    title: 'Interstellar',
    overview: 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity\'s survival.',
    posterPath: '/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg',
    backdropPath: '/xJHokMbljvjADYdit5fK5VQsXEG.jpg',
    releaseDate: '2014-11-05',
    voteAverage: 8.4,
    voteCount: 31234,
    genres: ['Adventure', 'Drama', 'Sci-Fi'],
    type: 'movie'
  },
  {
    id: 8,
    tmdbId: 155,
    title: 'The Dark Knight',
    overview: 'Batman raises the stakes in his war on crime with the help of Lt. Jim Gordon and District Attorney Harvey Dent.',
    posterPath: '/qJ2tW6WMUDux911r6m7haRef0WH.jpg',
    backdropPath: '/hkBaDkMWbLaf8B1lsWsKX7Ew3Xq.jpg',
    releaseDate: '2008-07-16',
    voteAverage: 8.5,
    voteCount: 29876,
    genres: ['Action', 'Crime', 'Drama'],
    type: 'movie'
  },
  {
    id: 9,
    tmdbId: 27205,
    title: 'Inception',
    overview: 'A thief who steals corporate secrets through dream-sharing technology is given the task of planting an idea.',
    posterPath: '/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg',
    backdropPath: '/s3TBrRGB1iav7gFOCNx3H31MoES.jpg',
    releaseDate: '2010-07-15',
    voteAverage: 8.4,
    voteCount: 33456,
    genres: ['Action', 'Sci-Fi', 'Thriller'],
    type: 'movie'
  },
  {
    id: 10,
    tmdbId: 66732,
    title: 'Stranger Things',
    overview: 'When a young boy vanishes, a small town uncovers a mystery involving secret experiments.',
    posterPath: '/49WJfeN0moxb9IPfGn8AIqMGskD.jpg',
    backdropPath: '/56v2KjBlU4XaOv9rVYEQypROD7P.jpg',
    releaseDate: '2016-07-15',
    voteAverage: 8.6,
    voteCount: 14567,
    genres: ['Drama', 'Mystery', 'Sci-Fi'],
    type: 'tv'
  },
  {
    id: 11,
    tmdbId: 1396,
    title: 'Breaking Bad',
    overview: 'A high school chemistry teacher diagnosed with cancer turns to manufacturing drugs.',
    posterPath: '/ggFHVNu6YYI5L9pCfOacjizRGt.jpg',
    backdropPath: '/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg',
    releaseDate: '2008-01-20',
    voteAverage: 8.9,
    voteCount: 21234,
    genres: ['Drama', 'Crime', 'Thriller'],
    type: 'tv'
  }
];

export const STATIC_WATCHLIST_MOVIES: MovieDto[] = [
  {
    id: 12,
    tmdbId: 603,
    title: 'The Matrix',
    overview: 'A computer hacker learns about the true nature of reality and his role in the war against its controllers.',
    posterPath: '/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg',
    backdropPath: '/fNG7i7RqMErkcqhohV2a6cV1Ehy.jpg',
    releaseDate: '1999-03-30',
    voteAverage: 8.2,
    voteCount: 22345,
    genres: ['Action', 'Sci-Fi'],
    type: 'movie'
  },
  {
    id: 13,
    tmdbId: 429,
    title: 'The Good, the Bad and the Ugly',
    overview: 'A bounty hunting scam joins two men in an uneasy alliance against a third in a race to find a fortune.',
    posterPath: '/bX2xnavhMYjWDoZp1VM6VnU1xwe.jpg',
    backdropPath: '/eRqfzsaZbLfghEgsLmDdCzCnfqL.jpg',
    releaseDate: '1966-12-23',
    voteAverage: 8.5,
    voteCount: 7234,
    genres: ['Western'],
    type: 'movie'
  },
  {
    id: 14,
    tmdbId: 424,
    title: 'Schindler\'s List',
    overview: 'In German-occupied Poland during World War II, industrialist Oskar Schindler becomes concerned for his Jewish workforce.',
    posterPath: '/sF1U4EUQS8YHUYjNl3pMGNIQyr0.jpg',
    backdropPath: '/loRmRzQXZeqG78TqZuyvSlEQfZb.jpg',
    releaseDate: '1993-12-15',
    voteAverage: 8.6,
    voteCount: 13456,
    genres: ['Drama', 'History', 'War'],
    type: 'movie'
  },
  {
    id: 15,
    tmdbId: 94605,
    title: 'Arcane',
    overview: 'Amid the stark discord of twin cities Piltover and Zaun, two sisters fight on rival sides of a war.',
    posterPath: '/fqldf2t8ztc9aiwn3k6mlX3tvRT.jpg',
    backdropPath: '/rkB4LyZHo1NHXFEDHl9vSD9r1lI.jpg',
    releaseDate: '2021-11-06',
    voteAverage: 9.0,
    voteCount: 3234,
    genres: ['Animation', 'Action', 'Adventure'],
    type: 'tv'
  },
  {
    id: 16,
    tmdbId: 60735,
    title: 'The Flash',
    overview: 'After being struck by lightning, Barry Allen wakes up from his coma to discover he\'s been given super speed.',
    posterPath: '/lJA2RCMfsWoskqlQhXPSLFQGXEJ.jpg',
    backdropPath: '/z59kJfcElR9eHO9rJbWp4qWMuee.jpg',
    releaseDate: '2014-10-07',
    voteAverage: 7.8,
    voteCount: 10234,
    genres: ['Drama', 'Sci-Fi'],
    type: 'tv'
  }
];

// Helper function to group movies by type
export function groupMoviesByType(movies: MovieDto[]): GroupedMovies {
  const moviesList: MovieDto[] = [];
  const tvSeriesList: MovieDto[] = [];

  movies.forEach(movie => {
    if (movie.type === 'movie') {
      moviesList.push(movie);
    } else if (movie.type === 'tv') {
      tvSeriesList.push(movie);
    }
  });

  return { movies: moviesList, tvSeries: tvSeriesList };
}

// Helper function to get poster URL from TMDB path
export function getMoviePosterUrl(posterPath: string): string {
  if (!posterPath) {
    return 'https://images.unsplash.com/photo-1489599732536-9a2b0caa395a?w=300&h=400&fit=crop';
  }
  return `https://image.tmdb.org/t/p/w500${posterPath}`;
}

// Helper function to extract year from release date
export function getMovieYear(releaseDate: string): string {
  if (!releaseDate) return 'Unknown';
  return new Date(releaseDate).getFullYear().toString();
}

// Helper function to format genres
export function getMovieGenres(genres: string[]): string {
  if (!genres || genres.length === 0) return 'Unknown';
  return genres.slice(0, 2).join(', ');
}
