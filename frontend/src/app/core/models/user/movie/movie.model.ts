import {Genre} from '../shared/shared-dtos';

export interface Movie {
  id: number;
  tmdbId: number;
  title: string;
  overview: string;
  posterPath: string;
  backdropPath: string;
  releaseDate: string;
  voteAverage: number;
  voteCount: number;
  originalLanguage: string;
  originalTitle: string;
  adult: boolean;
  status: string;
  tagline: string;
  runtime: number;
  genres: string[];
  type: string; // 'movie' or 'tv'
  category: string;
  isInUserList: boolean;
}


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
