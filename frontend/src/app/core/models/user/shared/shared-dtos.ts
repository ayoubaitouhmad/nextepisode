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


export interface TitleStatus {
  isFavorite: boolean;
  isWatched: boolean;
  isInWatchlist: boolean;
}


export interface UserTitleRequest {
  tmdbId: number;
  category: 'FAVORITE' | 'WATCHED' | 'WATCHLIST';
  action: 'ADD' | 'REMOVE';
}


