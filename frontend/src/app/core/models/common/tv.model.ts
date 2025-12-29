import {Genre, StreamingService} from './shared-dtos';


export interface UserTvShow {
  id: number;
  name: string;
  created_at: string;      // when added to list
  watched_at?: string;     // only for watched movies
  release_date: string;
  poster_path: string;
  genres: Genre[];
}

export interface UserTvShowList {
  page: number;
  total_pages: number;
  results: UserTvShow[];
  total_results: number;      // when added to list
}

export interface TMDBTvSeries {
  id: number;
  name: string;  // TV shows use 'name' instead of 'title'
  original_name: string;
  overview: string;
  poster_path: string;
  backdrop_path: string;
  first_air_date: string;  // Instead of release_date
  vote_average: number;
  vote_count: number;
  genre_ids: number[];
  origin_country: string[];
  original_language: string;
  popularity: number;
}

export interface TMDBTvSeriesResponse {
  page: number;
  results: TMDBTvSeries[];
  total_pages: number;
  total_results: number;
}

export interface TvSeries {
  id: string;
  title: string;  // We'll use 'title' for consistency with Movie interface
  rating: number;
  year: number;
  genres: Genre[];
  overview: string;
  backdrop_path: string;
  poster_path: string;
  releaseDate: string;
  voteCount: number;
  originalLanguage: string;
  originCountry: string[];
  streamingService?: StreamingService;
}
