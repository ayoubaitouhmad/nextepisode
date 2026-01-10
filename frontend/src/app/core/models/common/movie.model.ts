import {Genre, WatchProviderCountry} from './shared-dtos';

// export interface Movie {
//   id: number;
//   tmdbId: number;
//   title: string;
//   overview: string;
//   posterPath: string;
//   backdropPath: string;
//   releaseDate: string;
//   voteAverage: number;
//   voteCount: number;
//   originalLanguage: string;
//   originalTitle: string;
//   adult: boolean;
//   status: string;
//   tagline: string;
//   runtime: number;
//   genres: string[];
//   type: string; // 'movie' or 'tv'
//   category: string;
//   isInUserList: boolean;
// }


export interface UserMovie {
  id: number;
  title: string;
  created_at: string;      // when added to list
  watched_at?: string;     // only for watched movies
  release_date: string;
  poster_path: string;
  genres: Genre[];
}

export interface UserMovieList {
  page: number;
  total_pages: number;
  results: UserMovie[];
  total_results: number;      // when added to list
}

export function getUserMovieGenreAsString(genres: Genre[]) {
  if (genres.length < 0) {
    return "";
  }
  return genres.map(genre => genre.name).join(", ");
}

export interface XMovie {
  id: number;
  title: string;
  adult: boolean;
  rating: number;
  popularity: number;
  year: number;
  genres: Genre[];
  overview: string;
  backdrop_path: string;
  poster_path: string;
  release_date: string;
  vote_count: number;
  vote_average: number;
  original_language: string;
  watch_providers?: WatchProviderCountry;
  video: boolean;
}


export interface TMDBMovie {
  id: number;
  title: string;
  original_title: string;
  overview: string;
  poster_path: string | null;
  backdrop_path: string | null;
  release_date: string;
  vote_average: number;
  vote_count: number;
  genre_ids: number[];
  adult: boolean;
  original_language: string;
  popularity: number;
}

export interface TMDBMovieResponse {
  page: number;
  results: TMDBMovie[];
  total_pages: number;
  total_results: number;
}


export interface MovieList {
  page: number;
  results: XMovie[];
  total_pages: number;
  total_results: number;
}

export interface MovieStatus {
  isFavorite: boolean,
  watched: boolean,
  inWatchlist: boolean
}

export interface MovieStatusRequest {
  movieId: number;
  category: MovieStatusCategory.favorite | MovieStatusCategory.watched | MovieStatusCategory.watchlist;
  action: MovieStatusAction.add | MovieStatusAction.remove;
}

export enum MovieStatusCategory {
  favorite = 'FAVORITE',
  watched = 'WATCHED',
  watchlist = 'WATCHLIST',
}

export enum MovieStatusAction {
  add = 'ADD',
  remove = 'REMOVE'
}

export interface MoviesStatus {
  moviesStatus: Map<number, MovieStatus>;
}

