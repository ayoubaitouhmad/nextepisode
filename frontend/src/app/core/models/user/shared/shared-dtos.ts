export interface Genre {
  id: number;
  name: string;
}

export interface UserMoviesAndTvShowStats {
  favoriteCount: number;
  watchedCount: number;
  watchlistCount: number;
}


export function getUserMovieGenreAsString(genres: Genre[]) {
  if (genres.length < 0) {
    return "";
  }
  return genres.map(genre => genre.name).join(", ");
}
